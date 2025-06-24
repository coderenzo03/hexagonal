document.getElementById("form-login").addEventListener("submit", function (e) {
  e.preventDefault();

  const loginData = {
    correo: document.getElementById("correo").value,
    contrasena: document.getElementById("contrasena").value,
    rol: document.getElementById("rol").value
  };

  fetch("http://localhost:8080/api/auth/login", {
    method: "POST",
    headers: {
      "Content-Type": "application/json"
    },
    body: JSON.stringify(loginData)
  })
    .then(res => {
      if (!res.ok) return res.json().then(err => { throw new Error(err.error); });
      return res.json();
    })
    .then(data => {
      localStorage.setItem("token", data.token);
      localStorage.setItem("rol", data.rol);

      if (data.rol === "ADMIN") {
        window.location.href = "admin/dashboard.html";
      } else if (data.rol === "TRABAJADOR") {
        window.location.href = "usuario/index.html";
      } else {
        alert("Rol no reconocido");
      }
    })
    .catch(err => alert("Error: " + err.message));
});
