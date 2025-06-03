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
        evtDiv.textContent = evt;
        evtDiv.title = "Clique para remover";
        evtDiv.onclick = (e) => {
          e.stopPropagation();
          removeEvent(dateStr, idx);
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
  const eventText = prompt(`Adicionar evento para ${dateStr}:`);
  if (eventText) {
    if (!events[dateStr]) {
      events[dateStr] = [];
    }
    events[dateStr].push(eventText);
    saveEvents();
    renderCalendar();
  }
}

function removeEvent(dateStr, index) {
  if (confirm(`Remover evento: "${events[dateStr][index]}"?`)) {
    events[dateStr].splice(index, 1);
    if (events[dateStr].length === 0) {
      delete events[dateStr];
    }
    saveEvents();
    renderCalendar();
  }
}

loadEvents();
renderCalendar();
