//const token = localStorage.getItem('token');
//if (!token) window.location.href = '/login';

// Elements
const tbody = document.querySelector('#notesTable tbody');
const noteModal = document.getElementById('noteModal');
const modalTitle = document.getElementById('modalTitle');
const noteIdInput = document.getElementById('noteId');
const titleInput = document.getElementById('noteTitle');
const contentInput = document.getElementById('noteContent');
const tagsInput = document.getElementById('noteTags');
const titleError = document.getElementById('titleError');
const contentError = document.getElementById('contentError');
const paginationDiv = document.getElementById('pagination');

let currentPage = 0;
const pageSize = 5;

async function fetchWithAuth(url, options = {}) {
    options.credentials = 'same-origin'; // sends cookies
    return fetch(url, options);
}

// -------------------- Load Notes --------------------
async function loadNotes(page = 0) {
    currentPage = page;
    const search = document.getElementById('search').value;
    const tags = document.getElementById('tagsFilter').value;
    const sort = document.getElementById('sort').value;

    let url = `/api/notes?page=${page}&size=${pageSize}&sort=${sort}`;
    if (search) url += `&q=${encodeURIComponent(search)}`;
    if (tags) {
        const tagsArray = tags.split(',').map(t => t.trim()).filter(Boolean);
        tagsArray.forEach(tag => {
            url += `&tags=${encodeURIComponent(tag)}`;
        });
    }
    //if (tags) url += `&tags=${encodeURIComponent(tags)}`;

    const res = await fetchWithAuth(url/*, { headers: { 'Authorization': 'Bearer ' + token } }*/);

    if (!res.ok) {
        alert('Failed to load notes');
        return;
    }
    const data = await res.json();

    tbody.innerHTML = '';
    data.content.forEach(note => {
        const tr = document.createElement('tr');
        tr.innerHTML = `
            <td>${note.title}</td>
            <td>${note.content}</td>
            <td>${note.tags.map(t => t.name).join(', ')}</td>
            <td>${note.createdAt}</td>
            <td>${note.updatedAt}</td>
            <td>
                <button onclick="openModal(${note.id}, '${note.title}', '${note.content}', '${note.tags.map(t => t.name).join(',')}')">Edit</button>
                ${note.deletedAt ?
                    `<button onclick="restoreNote(${note.id})">Restore</button>` :
                    `<button onclick="deleteNote(${note.id})">Delete</button>`}
            </td>
        `;
        tbody.appendChild(tr);
    });

    // Pagination
    paginationDiv.innerHTML = '';
    for (let i = 0; i < data.totalPages; i++) {
        const btn = document.createElement('button');
        btn.innerText = i + 1;
        if (i === currentPage) btn.disabled = true;
        btn.onclick = () => loadNotes(i);
        paginationDiv.appendChild(btn);
    }
}

// -------------------- Filters --------------------
document.getElementById('applyFilter').addEventListener('click', () => loadNotes(0));

// -------------------- Modal --------------------
function openModal(id = null, title = '', content = '', tags = '') {
    modalTitle.innerText = id ? 'Edit Note' : 'Create Note';
    noteIdInput.value = id || '';
    titleInput.value = title;
    contentInput.value = content;
    tagsInput.value = tags;
    titleError.textContent = '';
    contentError.textContent = '';
    noteModal.style.display = 'block';
}

function closeModal() {
    noteModal.style.display = 'none';
}

document.getElementById('createNoteBtn').addEventListener('click', () => {
    openModal(); // no arguments = create mode
});

// -------------------- Save Note --------------------
document.getElementById('saveNote').addEventListener('click', async () => {
    const id = noteIdInput.value;
    const title = titleInput.value.trim();
    const content = contentInput.value.trim();
    const tags = tagsInput.value.split(',').map(t => t.trim()).filter(Boolean);

    titleError.textContent = '';
    contentError.textContent = '';

    if (!title) { titleError.textContent = 'Title is required'; return; }
    if (!content) { contentError.textContent = 'Content is required'; return; }

    const method = id ? 'PUT' : 'POST';
    const url = id ? `/api/notes/${id}` : '/api/notes';

    const res = await fetchWithAuth(url, {
        method,
        headers: {
            'Content-Type': 'application/json'
//            'Authorization': 'Bearer ' + token
        },
        body: JSON.stringify({ title, content, tags })
    });

    if (res.ok) {
        closeModal();
        loadNotes(currentPage);
    } else {
        const err = await res.json();
        alert(err.message || 'Failed to save note');
    }
});

// -------------------- Delete / Restore --------------------
async function deleteNote(id) {
    if (!confirm('Are you sure you want to delete this note?')) return;
    const res = await fetchWithAuth(`/api/notes/${id}`, {
        method: 'DELETE'
    });
    if (res.ok) loadNotes(currentPage);
    else alert('Failed to delete note');
}

async function restoreNote(id) {
    const res = await fetchWithAuth(`/api/notes/${id}/restore`, {
        method: 'PUT'
    });
    if (res.ok) loadNotes(currentPage);
    else alert('Failed to restore note');
}

// -------------------- Log out --------------------
document.getElementById('logoutBtn').addEventListener('click', () => {
    fetch('/logout', { method: 'POST', credentials: 'same-origin' })
        .then(() => window.location.href = '/auth/login');
});

// -------------------- Initial Load --------------------
loadNotes();
