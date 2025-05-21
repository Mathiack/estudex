function isMobile() {
    return window.innerWidth <= 800;
}

function isPc() {
    return !isMobile();
}

function toggleDropdown(menuId) {
    const menu = document.getElementById(menuId);
    const dropdown = menu.parentElement;

    document.querySelectorAll('.dropdown').forEach(d => {
        if (d !== dropdown) {
            d.classList.remove('show');
        }
    });

    dropdown.classList.toggle('show');

    document.addEventListener('click', closeDropdownOnClickOutside);
}

function closeDropdownOnClickOutside(event) {
    const dropdowns = document.querySelectorAll('.dropdown');
    dropdowns.forEach(dropdown => {
        if (!dropdown.contains(event.target)) {
            dropdown.classList.remove('show');
        }
    });
    document.removeEventListener('click', closeDropdownOnClickOutside);
}

function toggleSidebar(sidebarId) {
    const sidebar = document.getElementById(sidebarId);

    document.querySelectorAll('.sidebar').forEach(s => {
        if (s !== sidebar) {
            s.classList.remove('show');
        }
    });

    sidebar.classList.toggle('show');
}

function showToast(message, icon = "") {
    const snackbar = document.createElement("div");
    snackbar.className = "toast show";
    snackbar.innerHTML = `<span class='icon' style='font-size:x-large'>${icon}</span><p>${message}</p>`;

    document.body.appendChild(snackbar);

    setTimeout(() => {
        snackbar.classList.remove("show");
        setTimeout(() => {
            snackbar.remove();
        }, 400);
    }, 3000);
}

function hideAllMenus() {
    document.querySelectorAll('.dropdown').forEach(dropdown => {
        dropdown.classList.remove('show');
    });
}

function hideAllSidebars() {
    document.querySelectorAll('.sidebar').forEach(sidebar => {
        sidebar.classList.remove('show');
    });
}

function promptString(title, defaultText = "") {
    return new Promise((resolve) => {
        // overlay
        const overlay = document.createElement('div');
        overlay.className = 'prompt-overlay';

        // dialog
        const dialog = document.createElement('div');
        dialog.className = 'prompt-dialog';

        // title
        const titleElement = document.createElement('p');
        titleElement.textContent = title;
        titleElement.className = 'prompt-title';
        dialog.appendChild(titleElement);

        // field
        const input = document.createElement('input');
        input.type = 'text';
        input.value = defaultText ? defaultText : ""
        input.className = 'prompt-input';
        dialog.appendChild(input);

        // buttons
        const buttonContainer = document.createElement('div');
        buttonContainer.className = 'prompt-buttons';

        const cancelButton = document.createElement('button');
        cancelButton.textContent = 'Cancel';
        cancelButton.className = 'prompt-button cancel';

        const submitButton = document.createElement('button');
        submitButton.textContent = 'Ok';
        submitButton.className = 'prompt-button submit';

        buttonContainer.appendChild(cancelButton);
        buttonContainer.appendChild(submitButton);
        dialog.appendChild(buttonContainer);

        overlay.appendChild(dialog);
        document.body.appendChild(overlay);

        input.focus();
        input.selectionStart = 0;
        input.selectionEnd = input.value.length;

        function closePrompt(result) {
            document.body.removeChild(overlay);
            resolve(result);
        }

        cancelButton.addEventListener('click', () => closePrompt(null));
        submitButton.addEventListener('click', () => closePrompt(input.value));

        overlay.addEventListener('keydown', (event) => {
            if (event.key === 'Enter') {
                closePrompt(input.value);
            } else if (event.key === 'Escape') {
                closePrompt(null);
            }
        });
    });
}

function promptMessage(htmlContent) {
    return new Promise((resolve) => {
        // overlay
        const overlay = document.createElement('div');
        overlay.className = 'prompt-overlay';

        // dialog
        const dialog = document.createElement('div');
        dialog.className = 'prompt-dialog';
        dialog.style.width = '100%';
        dialog.style.maxWidth = '500px';

        // html content
        const content = document.createElement('div');
        content.innerHTML = htmlContent;
        content.style.marginBottom = '15px';
        dialog.appendChild(content);

        // ok button
        const okButton = document.createElement('button');
        okButton.textContent = 'Ok';
        okButton.className = 'prompt-button submit';

        dialog.appendChild(okButton);
        overlay.appendChild(dialog);
        document.body.appendChild(overlay);

        function closePrompt() {
            document.body.removeChild(overlay);
            resolve();
        }

        okButton.addEventListener('click', closePrompt);

        overlay.addEventListener('keydown', (event) => {
            if (event.key === 'Enter' || event.key === 'Escape') {
                closePrompt();
            }
        });

        okButton.focus();
    });
}

function promptMessageFromFile(filePath, buttonText = "Ok") {
    return new Promise((resolve, reject) => {
        fetch(filePath)
            .then(response => {
                if (!response.ok) {
                    throw new Error(`Failed loading file: ${response.statusText}`);
                }
                return response.text();
            })
            .then(htmlContent => {
                // overlay
                const overlay = document.createElement('div');
                overlay.className = 'prompt-overlay';

                // dialog
                const dialog = document.createElement('div');
                dialog.className = 'prompt-dialog';
                dialog.style.width = '100%';
                dialog.style.maxWidth = '500px';

                // html content
                const content = document.createElement('div');
                content.innerHTML = htmlContent;
                content.style.marginBottom = '15px';
                dialog.appendChild(content);

                // ok button
                const okButton = document.createElement('button');
                okButton.textContent = buttonText;
                okButton.className = 'prompt-button';

                dialog.appendChild(okButton);
                overlay.appendChild(dialog);
                document.body.appendChild(overlay);

                function closePrompt() {
                    document.body.removeChild(overlay);
                    resolve();
                }

                okButton.addEventListener('click', closePrompt);

                overlay.addEventListener('keydown', (event) => {
                    if (event.key === 'Enter' || event.key === 'Escape') {
                        closePrompt();
                    }
                });

                okButton.focus();
            })
            .catch(error => {
                reject(error);
            });
    });
}

function promptConfirm(message) {
    return new Promise((resolve) => {
        // overlay
        const overlay = document.createElement('div');
        overlay.className = 'prompt-overlay';

        // dialog
        const dialog = document.createElement('div');
        dialog.className = 'prompt-dialog';
        dialog.style.width = '100%';
        dialog.style.maxWidth = '400px';

        // message
        const text = document.createElement('p');
        text.textContent = message;
        text.className = 'prompt-title';
        dialog.appendChild(text);

        // buttons
        const buttonContainer = document.createElement('div');
        buttonContainer.className = 'prompt-buttons';

        const yesButton = document.createElement('button');
        yesButton.textContent = 'Yes';
        yesButton.className = 'prompt-button submit';

        const noButton = document.createElement('button');
        noButton.textContent = 'No';
        noButton.className = 'prompt-button cancel';

        buttonContainer.appendChild(noButton);
        buttonContainer.appendChild(yesButton);
        dialog.appendChild(buttonContainer);

        overlay.appendChild(dialog);
        document.body.appendChild(overlay);

        function closePrompt(result) {
            document.body.removeChild(overlay);
            resolve(result);
        }

        yesButton.addEventListener('click', () => closePrompt(true));
        noButton.addEventListener('click', () => closePrompt(false));

        overlay.addEventListener('keydown', (event) => {
            if (event.key === 'Enter') {
                closePrompt(true);
            } else if (event.key === 'Escape') {
                closePrompt(false);
            }
        });

        yesButton.focus();
    });
}

function newTimer() {
    
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