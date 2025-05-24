function saveDataTimer() {
    localStorage.setItem('timer', JSON.stringify(timer));
}

function loadDataTimer() {
    const data = localStorage.getItem('timer');
    timer = data ? JSON.parse(data) : [];
}

function renderTimer() {
    const sidebar = document.getElementById("files");
    sidebar.innerHTML = '';
    timer.forEach((timer, index) => {
        const button = document.createElement("button");
        button.className = "item";
        button.textContent = `${timer.nome} - ${livro.autor}`;

        //   !!adicionar pequeno texto abaixo do título com o tipo de timer!!

        button.addEventListener('contextmenu', (e) => {
            e.preventDefault();
            showContextMenu(e.pageX, e.pageY, index);
        });

        sidebar.appendChild(button);
    });
}

function promptPomodoro() {
    return new Promise((resolve) => {
        const link = document.createElement('link');
        link.rel = 'stylesheet';
        link.href = '/styles/pomodoro.css';
        document.head.appendChild(link);

        const overlay = document.createElement('div');
        overlay.className = 'prompt-overlay';

        const dialog = document.createElement('div');
        dialog.className = 'prompt-dialog';

        const titleElement = document.createElement('p');
        titleElement.className = 'prompt-title';
        titleElement.textContent = "Configurações do Pomodoro";
        dialog.appendChild(titleElement);

        const focusTimeContainer = document.createElement('div');
        focusTimeContainer.className = 'time-row';

        const hourCol = document.createElement('div');
        hourCol.className = 'time-col';

        const hourUp = document.createElement('button');
        hourUp.className = 'icon-button';
        hourUp.textContent = '▲';

        const hourField = document.createElement('input');
        hourField.type = 'number';
        hourField.className = 'time-input';
        hourField.min = 0;
        hourField.max = 23;
        hourField.placeholder = '00';

        const hourDown = document.createElement('button');
        hourDown.className = 'icon-button';
        hourDown.textContent = '▼';

        hourCol.appendChild(hourUp);
        hourCol.appendChild(hourField);
        hourCol.appendChild(hourDown);

        const minuteCol = document.createElement('div');
        minuteCol.className = 'time-col';

        const minuteUp = document.createElement('button');
        minuteUp.className = 'icon-button';
        minuteUp.textContent = '▲';

        const minuteField = document.createElement('input');
        minuteField.type = 'number';
        minuteField.className = 'time-input';
        minuteField.min = 0;
        minuteField.max = 59;
        minuteField.placeholder = '00';

        const minuteDown = document.createElement('button');
        minuteDown.className = 'icon-button';
        minuteDown.textContent = '▼';

        minuteCol.appendChild(minuteUp);
        minuteCol.appendChild(minuteField);
        minuteCol.appendChild(minuteDown);

        focusTimeContainer.appendChild(hourCol);

        const separator = document.createElement('span');
        separator.textContent = ':';
        separator.className = 'time-separator';
        focusTimeContainer.appendChild(separator);

        focusTimeContainer.appendChild(minuteCol);
        dialog.appendChild(focusTimeContainer);

        const restTimeContainer = document.createElement('div');
        restTimeContainer.className = 'time-row';

        const restHourCol = document.createElement('div');
        restHourCol.className = 'time-col';

        const restHourUp = document.createElement('button');
        restHourUp.className = 'icon-button';
        restHourUp.textContent = 'keyboard_arrow_up';

        const restHourField = document.createElement('input');
        restHourField.type = 'number';
        restHourField.className = 'time-input';
        restHourField.min = 0;
        restHourField.max = 23;
        restHourField.placeholder = '00';

        const restHourDown = document.createElement('button');
        restHourDown.className = 'icon-button';
        restHourDown.textContent = 'keyboard_arrow_down';

        restHourCol.appendChild(restHourUp);
        restHourCol.appendChild(restHourField);
        restHourCol.appendChild(restHourDown);

        const restMinuteCol = document.createElement('div');
        restMinuteCol.className = 'time-col';

        const restMinuteUp = document.createElement('button');
        restMinuteUp.className = 'icon-button';
        restMinuteUp.textContent = 'keyboard_arrow_up';

        const restMinuteField = document.createElement('input');
        restMinuteField.type = 'number';
        restMinuteField.className = 'time-input';
        restMinuteField.min = 0;
        restMinuteField.max = 59;
        restMinuteField.placeholder = '00';

        const restMinuteDown = document.createElement('button');
        restMinuteDown.className = 'icon-button';
        restMinuteDown.textContent = 'keyboard_arrow_down';

        restMinuteCol.appendChild(restMinuteUp);
        restMinuteCol.appendChild(restMinuteField);
        restMinuteCol.appendChild(restMinuteDown);

        restTimeContainer.appendChild(restHourCol);

        const restSeparator = document.createElement('span');
        restSeparator.textContent = ':';
        restSeparator.className = 'time-separator';
        restTimeContainer.appendChild(restSeparator);

        restTimeContainer.appendChild(restMinuteCol);
        dialog.appendChild(restTimeContainer);

        const buttonContainer = document.createElement('div');
        buttonContainer.className = 'prompt-buttons';

        const cancelButton = document.createElement('button');
        cancelButton.textContent = 'Cancelar';
        cancelButton.className = 'prompt-button cancel';

        const submitButton = document.createElement('button');
        submitButton.textContent = 'Ok';
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
            const hours = parseInt(hourField.value) || 0;
            const minutes = parseInt(minuteField.value) || 0;
            const restHours = parseInt(restHourField.value) || 0;
            const restMinutes = parseInt(restMinuteField.value) || 0;

            const sidebar = document.querySelector(".sidebar-timer");
            if (sidebar) {
                const li = document.createElement("li");
                li.className = "sidebar-item";
                li.textContent = `Pomodoro`;
                sidebar.appendChild(li);
            }

            closePrompt({ hours, minutes, restHours, restMinutes });
        });

        hourUp.onclick = () => hourField.value = Math.min((parseInt(hourField.value) || 0) + 1, 23);
        hourDown.onclick = () => hourField.value = Math.max((parseInt(hourField.value) || 0) - 1, 0);
        minuteUp.onclick = () => minuteField.value = Math.min((parseInt(minuteField.value) || 0) + 1, 59);
        minuteDown.onclick = () => minuteField.value = Math.max((parseInt(minuteField.value) || 0) - 1, 0);

        restHourUp.onclick = () => restHourField.value = Math.min((parseInt(restHourField.value) || 0) + 1, 23);
        restHourDown.onclick = () => restHourField.value = Math.max((parseInt(restHourField.value) || 0) - 1, 0);
        restMinuteUp.onclick = () => restMinuteField.value = Math.min((parseInt(restMinuteField.value) || 0) + 1, 59);
        restMinuteDown.onclick = () => restMinuteField.value = Math.max((parseInt(restMinuteField.value) || 0) - 1, 0);

        overlay.tabIndex = -1;
        overlay.focus();

        overlay.addEventListener('keydown', (event) => {
            if (event.key === 'Enter') {
                submitButton.click();
            } else if (event.key === 'Escape') {
                closePrompt(null);
            }
        });
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