function saveDataNotas() {
    localStorage.setItem('notas', JSON.stringify(notas));
}

function loadDataNotas() {
    const data = localStorage.getItem('notas');
    notas = data ? JSON.parse(data) : [];
}

function renderNotas() {
    const sidebar = document.getElementById("files");
    sidebar.innerHTML = '';
    notas.forEach((livro, index) => {
        const button = document.createElement("button");
        button.className = "item";
        button.textContent = `${livro.nome} - ${livro.autor}`;

        button.addEventListener('contextmenu', (e) => {
            e.preventDefault();
            showContextMenu(e.pageX, e.pageY, index);
        });

        sidebar.appendChild(button);
    });
}

function showContextMenu(x, y, index) {
    closeContextMenu();
    const menu = document.createElement('div');
    menu.className = 'custom-context-menu';
    menu.style.top = `${y}px`;
    menu.style.left = `${x}px`;

    const editBtn = document.createElement('button');
    editBtn.textContent = 'Editar';
    editBtn.onclick = () => {
        closeContextMenu();
        // funcao para editar
    };

    const deleteBtn = document.createElement('button');
    deleteBtn.textContent = 'Excluir';
    deleteBtn.onclick = () => {
        closeContextMenu();
        //funcao para excluir
    };

    menu.appendChild(editBtn);
    menu.appendChild(deleteBtn);
    document.body.appendChild(menu);

    document.addEventListener('click', closeContextMenu);
}