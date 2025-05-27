let biblioteca = [];

function saveDataBiblioteca() {
    localStorage.setItem('biblioteca', JSON.stringify(biblioteca));
}

function loadDataBiblioteca() {
    const data = localStorage.getItem('biblioteca');
    biblioteca = data ? JSON.parse(data) : [];
}

function renderBiblioteca() {
    const sidebar = document.getElementById("files");
    sidebar.innerHTML = '';
    biblioteca.forEach((livro, index) => {
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

function deleteLivro(index) {
    biblioteca.splice(index, 1);
    saveDataBiblioteca();
    renderBiblioteca();
}

function editLivro(index) {
    const original = biblioteca[index];
    const snapshotLength = biblioteca.length;

    promptBiblioteca().then(() => {
        const addedLivro = biblioteca[biblioteca.length - 1];

        biblioteca[index] = addedLivro;
        if (biblioteca.length > snapshotLength) {
            biblioteca.pop();
        }

        saveDataBiblioteca();
        renderBiblioteca();
    });
}

document.addEventListener('DOMContentLoaded', () => {
    loadDataBiblioteca();
    renderBiblioteca();
});


function promptBiblioteca() {
    return new Promise((resolve) => {
        const overlay = document.createElement('div');
        overlay.className = 'prompt-overlay';

        const dialog = document.createElement('div');
        dialog.className = 'prompt-dialog biblioteca';

        const titleElement = document.createElement('p');
        titleElement.className = 'prompt-title';
        titleElement.textContent = "Cadastro de Livro";
        dialog.appendChild(titleElement);

        function createInputField(labelText, placeholder, isOptional = false) {
            const container = document.createElement('div');
            container.className = 'input-group';

            const label = document.createElement('label');
            label.textContent = labelText + (isOptional ? ' (opcional)' : '');
            container.appendChild(label);

            const input = document.createElement('input');
            input.type = 'text';
            input.placeholder = placeholder;
            input.className = 'prompt-input biblioteca';
            container.appendChild(input);

            return { container, input };
        }

        function createTextAreaField(labelText, placeholder, isOptional = false) {
            const container = document.createElement('div');
            container.className = 'input-group';

            const label = document.createElement('label');
            label.textContent = labelText + (isOptional ? ' (opcional)' : '');
            container.appendChild(label);

            const textarea = document.createElement('textarea');
            textarea.placeholder = placeholder;
            textarea.className = 'prompt-textarea';
            container.appendChild(textarea);

            return { container, textarea };
        }

        const { container: nameGroup, input: nameInput } = createInputField('Nome do livro', '');
        const { container: authorGroup, input: authorInput } = createInputField('Autor', '');
        const { container: yearGroup, input: yearInput } = createInputField('Ano de lançamento', '', true);
        const { container: pagesGroup, input: pagesInput } = createInputField('Páginas', '', true);
        const { container: genreGroup, input: genreInput } = createInputField('Gênero', '', true);
        const { container: descGroup, textarea: descInput } = createTextAreaField('Descrição', '', true);

        dialog.appendChild(nameGroup);
        dialog.appendChild(authorGroup);
        dialog.appendChild(yearGroup);
        dialog.appendChild(pagesGroup);
        dialog.appendChild(genreGroup);
        dialog.appendChild(descGroup);

        const uploadButton = document.createElement('button');
        uploadButton.textContent = 'Adicionar imagem';
        uploadButton.className = 'prompt-button upload';
        dialog.appendChild(uploadButton);

        const buttonContainer = document.createElement('div');
        buttonContainer.className = 'prompt-buttons';

        const cancelButton = document.createElement('button');
        cancelButton.textContent = 'Cancelar';
        cancelButton.className = 'prompt-button cancel';

        const submitButton = document.createElement('button');
        submitButton.textContent = 'Salvar';
        submitButton.className = 'prompt-button submit';

        buttonContainer.appendChild(cancelButton);
        buttonContainer.appendChild(submitButton);
        dialog.appendChild(buttonContainer);

        overlay.appendChild(dialog);
        document.body.appendChild(overlay);

        function closePrompt(result) {
            document.body.removeChild(overlay);
            resolve(result);
        }

        cancelButton.addEventListener('click', () => closePrompt(null));
        submitButton.addEventListener('click', () => {
            const nome = nameInput.value.trim();
            const autor = authorInput.value.trim();
            const ano = yearInput.value.trim();
            const paginas = pagesInput.value.trim();
            const genero = genreInput.value.trim();
            const descricao = descInput.value.trim();

            if (!nome) {
                showToast("Nome é obrigatório!", "error");
                return;
            } else if (!autor) {
                autor = "Autor desconhecido";
                return;
            }
            const sidebar = document.getElementById("files");
            if (sidebar) {
                const button = document.createElement("button");
                button.className = "item";
                button.textContent = `${nome} - ${autor}`;
                sidebar.appendChild(button);
            }

            const novoLivro = {
                nome,
                autor,
                ano: ano || null,
                paginas: paginas || null,
                genero: genero || null,
                descricao: descricao || null
            };

            biblioteca.push(novoLivro);
            saveDataBiblioteca(); // <- salva no localStorage
            closePrompt(null);
        });

        overlay.tabIndex = -1;
        overlay.focus();

        document.addEventListener('keydown', (event) => {
            const activeElement = document.activeElement;
            const isEditable = activeElement.isContentEditable ||
                activeElement.tagName === 'INPUT' ||
                activeElement.tagName === 'TEXTAREA';

            if (event.key === 'Enter' && !isEditable) {
                submitButton.click();
            }
        });
    });
}

function showContextMenu(x, y, index) {
    closeContextMenu();
    const menu = document.createElement('div');
    menu.className = 'custom-context-menu';
    menu.style.position = 'absolute';
    menu.style.top = `${y}px`;
    menu.style.left = `${x}px`;
    menu.style.zIndex = 1000;

    const editBtn = document.createElement('button');
    editBtn.textContent = 'Editar';
    editBtn.onclick = () => {
        closeContextMenu();
        editLivro(index);
    };

    const deleteBtn = document.createElement('button');
    deleteBtn.textContent = 'Excluir';
    deleteBtn.onclick = () => {
        closeContextMenu();
        deleteLivro(index);
    };

    menu.appendChild(editBtn);
    menu.appendChild(deleteBtn);
    document.body.appendChild(menu);

    setTimeout(() => {
        document.addEventListener('click', closeContextMenu, { once: true });
    }, 0);
}