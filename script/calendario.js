const monthYearEl = document.getElementById('month-year');
const calendarDays = document.getElementById('calendar-days');
let currentDate = new Date();
let events = {};

const weekDays = ['Dom', 'Seg', 'Ter', 'Qua', 'Qui', 'Sex', 'Sáb'];

function loadEvents() {
  const saved = localStorage.getItem('calendarEvents');
  events = saved ? JSON.parse(saved) : {};
}

function saveEvents() {
  localStorage.setItem('calendarEvents', JSON.stringify(events));
}

function renderCalendar() {
  calendarDays.innerHTML = '';
  const year = currentDate.getFullYear();
  const month = currentDate.getMonth();

  const firstDay = new Date(year, month, 1).getDay();
  const daysInMonth = new Date(year, month + 1, 0).getDate();

  // Header
  weekDays.forEach(day => {
    const div = document.createElement('div');
    div.className = 'weekday';
    div.textContent = day;
    calendarDays.appendChild(div);
  });

  // Espaços vazios antes do 1º dia
  for (let i = 0; i < firstDay; i++) {
    calendarDays.appendChild(document.createElement('div'));
  }

  // Dias do mês
  for (let day = 1; day <= daysInMonth; day++) {
    const dateStr = `${year}-${String(month + 1).padStart(2, '0')}-${String(day).padStart(2, '0')}`;
    const div = document.createElement('div');
    div.className = 'day';
    div.textContent = day;
    div.onclick = () => addEvent(dateStr);

    if (events[dateStr]) {
      events[dateStr].forEach((evt, idx) => {
        const evtDiv = document.createElement('div');
        evtDiv.className = 'event';
        evtDiv.textContent = evt.title;
        evtDiv.style.backgroundColor = evt.color || '#ffeb3b';
        evtDiv.style.color = getContrastColor(evt.color || '#ffeb3b');
        evtDiv.title = "Clique para remover (ou botão direito para mais opções)";

        evtDiv.onclick = (e) => {
          e.stopPropagation();
          removeEvent(dateStr, idx);
        };

        evtDiv.oncontextmenu = (e) => {
          e.preventDefault();
          showContextMenu(e.pageX, e.pageY, dateStr, idx);
        };

        div.appendChild(evtDiv);
      });
    }


    calendarDays.appendChild(div);
  }

  monthYearEl.textContent = `${currentDate.toLocaleString('pt-BR', { month: 'long' })} ${year}`;
}

function changeMonth(offset) {
  currentDate.setMonth(currentDate.getMonth() + offset);
  renderCalendar();
}

function addEvent(dateStr) {
  const modal = document.createElement('div');
  modal.style.position = 'fixed';
  modal.style.top = '0';
  modal.style.left = '0';
  modal.style.width = '100%';
  modal.style.height = '100%';
  modal.style.backgroundColor = 'rgba(0,0,0,0.5)';
  modal.style.display = 'flex';
  modal.style.alignItems = 'center';
  modal.style.justifyContent = 'center';
  modal.innerHTML = `
    <div style="background:white;padding:20px;border-radius:10px;max-width:300px;width:100%;text-align:left;">
      <h3>Adicionar Evento</h3>
      <label>Título:</label><br>
      <input type="text" id="event-title" style="width:100%;margin-bottom:10px;"><br>
      <label>Cor:</label><br>
      <input type="color" id="event-color" style="width:100%;margin-bottom:10px;"><br>
      <button id="save-event">Salvar</button>
      <button id="cancel-event" style="float:right;">Cancelar</button>
    </div>
  `;

  document.body.appendChild(modal);

  document.getElementById('cancel-event').onclick = () => {
    document.body.removeChild(modal);
  };

  document.getElementById('save-event').onclick = () => {
    const title = document.getElementById('event-title').value.trim();
    const color = document.getElementById('event-color').value;

    if (!title) {
      alert('Por favor, insira um título para o evento.');
      return;
    }

    if (!events[dateStr]) {
      events[dateStr] = [];
    }

    events[dateStr].push({ title, color });
    saveEvents();
    renderCalendar();
    document.body.removeChild(modal);
  };
}

document.body.addEventListener('contextmenu', (e) => {
  const isEventElement = e.target.classList.contains('event');
  if (!isEventElement) {
    const existingMenu = document.getElementById('context-menu');
    if (existingMenu) existingMenu.remove();
  }
});

function showContextMenu(x, y, dateStr, index) {
  // Remove menu antigo se houver
  const existingMenu = document.getElementById('context-menu');
  if (existingMenu) existingMenu.remove();

  // Cria novo menu
  const menu = document.createElement('div');
  menu.id = 'context-menu';
  menu.style.position = 'absolute';
  menu.style.top = `${y}px`;
  menu.style.left = `${x}px`;
  menu.style.background = '#fff';
  menu.style.border = '1px solid #ccc';
  menu.style.padding = '4px 0';
  menu.style.boxShadow = '0 2px 10px rgba(0,0,0,0.2)';
  menu.style.borderRadius = '6px';
  menu.style.width = '140px';
  menu.style.fontSize = '14px';
  menu.style.zIndex = 10000;

  // Item: Editar
  const editOption = document.createElement('div');
  editOption.textContent = '✏️ Editar';
  editOption.style.padding = '6px 10px';
  editOption.style.cursor = 'pointer';
  editOption.onmouseenter = () => editOption.style.background = '#eee';
  editOption.onmouseleave = () => editOption.style.background = 'transparent';
  editOption.onclick = () => {
    document.body.removeChild(menu);
    editEvent(dateStr, index);
  };

  // Item: Excluir
  const deleteOption = document.createElement('div');
  deleteOption.textContent = '🗑️ Excluir';
  deleteOption.style.padding = '6px 10px';
  deleteOption.style.cursor = 'pointer';
  deleteOption.onmouseenter = () => deleteOption.style.background = '#eee';
  deleteOption.onmouseleave = () => deleteOption.style.background = 'transparent';
  deleteOption.onclick = () => {
    document.body.removeChild(menu);
    removeEvent(dateStr, index);
  };

  menu.appendChild(editOption);
  menu.appendChild(deleteOption);
  document.body.appendChild(menu);

  // Remove menu ao clicar em qualquer lugar fora dele
  const removeOnClick = (e) => {
    if (!menu.contains(e.target)) {
      menu.remove();
      document.removeEventListener('click', removeOnClick);
    }
  };
  document.addEventListener('click', removeOnClick);
}

function editEvent(dateStr, index) {
  const evt = events[dateStr][index];

  const modal = document.createElement('div');
  modal.style.position = 'fixed';
  modal.style.top = '0';
  modal.style.left = '0';
  modal.style.width = '100%';
  modal.style.height = '100%';
  modal.style.backgroundColor = 'rgba(0,0,0,0.5)';
  modal.style.display = 'flex';
  modal.style.alignItems = 'center';
  modal.style.justifyContent = 'center';
  modal.innerHTML = `
    <div style="background:white;padding:20px;border-radius:10px;max-width:300px;width:100%;text-align:left;">
      <h3>Editar Evento</h3>
      <label>Título:</label><br>
      <input type="text" id="event-title" value="${evt.title}" style="width:100%;margin-bottom:10px;"><br>
      <label>Cor:</label><br>
      <input type="color" id="event-color" value="${evt.color}" style="width:100%;margin-bottom:10px;"><br>
      <button id="save-event">Salvar</button>
      <button id="cancel-event" style="float:right;">Cancelar</button>
    </div>
  `;

  document.body.appendChild(modal);

  evtDiv.oncontextmenu = (e) => {
    e.preventDefault();
    showContextMenu(e.pageX, e.pageY, dateStr, idx);
  };

  document.getElementById('cancel-event').onclick = () => {
    document.body.removeChild(modal);
  };

  document.getElementById('save-event').onclick = () => {
    const title = document.getElementById('event-title').value.trim();
    const color = document.getElementById('event-color').value;

    if (!title) {
      alert('Por favor, insira um título válido.');
      return;
    }

    events[dateStr][index] = { title, color };
    saveEvents();
    renderCalendar();
    document.body.removeChild(modal);
  };
}

function removeEvent(dateStr, index) {
  if (!events[dateStr]) return;

  events[dateStr].splice(index, 1);

  if (events[dateStr].length === 0) {
    delete events[dateStr];
  }

  saveEvents();
  renderCalendar();
}

function getContrastColor(hex) {
  // Remove # se existir
  hex = hex.replace('#', '');

  // Converte para RGB
  const r = parseInt(hex.substring(0, 2), 16);
  const g = parseInt(hex.substring(2, 4), 16);
  const b = parseInt(hex.substring(4, 6), 16);

  // Calcula luminância perceptual
  const brightness = (r * 299 + g * 587 + b * 114) / 1000;

  // Retorna preto ou branco baseado na luminância
  return brightness > 128 ? '#000000' : '#FFFFFF';
}

loadEvents();
renderCalendar();
