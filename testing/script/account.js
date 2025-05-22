// Importar Firebase
import { initializeApp } from "https://www.gstatic.com/firebasejs/9.6.4/firebase-app.js";
import { getAuth, createUserWithEmailAndPassword, signInWithEmailAndPassword, signOut, onAuthStateChanged } from "https://www.gstatic.com/firebasejs/9.6.4/firebase-auth.js";

// Inicializar Firebase (certifique-se de que firebaseConfig está em firebase-config.js)
const app = initializeApp(firebaseConfig);
const auth = getAuth(app);

// Elementos do DOM (verificar existência antes de usar)
const signupForm = document.getElementById("signup-form");
const loginForm = document.getElementById("login-form");
const signoutButton = document.getElementById("signout");
const message = document.getElementById("message");

// Cadastro com e-mail/senha
if (signupForm) {
  signupForm.addEventListener("submit", (e) => {
    e.preventDefault();
    const email = document.getElementById("email").value;
    const password = document.getElementById("password").value;

    createUserWithEmailAndPassword(auth, email, password)
      .then((userCredential) => {
        message.textContent = `Conta criada com sucesso para ${userCredential.user.email}!`;
        message.classList.remove("error");
        signupForm.reset();
        // Redirecionar para a página de login ou logged após cadastro (opcional)
        window.location.href = "login.html"; // Ajuste conforme sua lógica
      })
      .catch((error) => {
        let errorMessage = "Erro ao cadastrar: ";
        switch (error.code) {
          case "auth/email-already-in-use":
            errorMessage += "Este e-mail já está em uso.";
            break;
          case "auth/invalid-email":
            errorMessage += "E-mail inválido.";
            break;
          case "auth/weak-password":
            errorMessage += "A senha deve ter pelo menos 6 caracteres.";
            break;
          default:
            errorMessage += error.message;
        }
        message.textContent = errorMessage;
        message.classList.add("error");
      });
  });
}

// Login com e-mail/senha
if (loginForm) {
  loginForm.addEventListener("submit", (e) => {
    e.preventDefault();
    const email = document.getElementById("login-email").value;
    const password = document.getElementById("login-password").value;

    signInWithEmailAndPassword(auth, email, password)
      .then((userCredential) => {
        message.textContent = `Login bem-sucedido para ${userCredential.user.email}!`;
        message.classList.remove("error");
        loginForm.reset();
        // Redirecionar para a página logada após login
        window.location.href = "logged.html"; // Ajuste conforme sua lógica
      })
      .catch((error) => {
        let errorMessage = "Erro no login: ";
        switch (error.code) {
          case "auth/user-not-found":
            errorMessage += "Usuário não encontrado.";
            break;
          case "auth/wrong-password":
            errorMessage += "Senha incorreta.";
            break;
          case "auth/invalid-email":
            errorMessage += "E-mail inválido.";
            break;
          default:
            errorMessage += error.message;
        }
        message.textContent = errorMessage;
        message.classList.add("error");
      });
  });
}

// Logout
if (signoutButton) {
  signoutButton.addEventListener("click", () => {
    signOut(auth)
      .then(() => {
        message.textContent = "Deslogado com sucesso!";
        message.classList.remove("error");
        // Redirecionar para a página inicial após logout
        window.location.href = "index.html"; // Ajuste conforme sua lógica
      })
      .catch((error) => {
        message.textContent = "Erro ao deslogar: " + error.message;
        message.classList.add("error");
      });
  });
}

// Monitorar estado de autenticação
onAuthStateChanged(auth, (user) => {
  if (user) {
    message.textContent = `Bem-vindo, ${user.displayName || user.email}!`;
    message.classList.remove("error");
    if (signoutButton) signoutButton.style.display = "block";
    if (signupForm) signupForm.style.display = "none";
    if (loginForm) loginForm.style.display = "none";
  } else {
    message.textContent = "Nenhum usuário logado.";
    message.classList.remove("error");
    if (signoutButton) signoutButton.style.display = "none";
    if (signupForm) signupForm.style.display = "block";
    if (loginForm) loginForm.style.display = "block";
  }
});