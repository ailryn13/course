const emailInput = document.getElementById('email');
const passwordInput = document.getElementById('password');
const emailError = document.getElementById('emailError');
const passwordError = document.getElementById('passwordError');
const loginBtn = document.getElementById('loginBtn');

emailInput.addEventListener('input', function() {
    const emailPattern = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;

    if (emailInput.value.length > 0 && !emailPattern.test(emailInput.value)) {
        emailError.style.display = 'block';
        emailInput.style.borderColor = 'red';
        loginBtn.disabled = true;
    } else {
        emailError.style.display = 'none';
        emailInput.style.borderColor = '#ccc';
        checkFormValidity();
    }
});

passwordInput.addEventListener('input', function() {
    if (passwordInput.value.trim() === "" && passwordInput.value.length > 0) {
        passwordError.style.display = 'block';
        passwordInput.style.borderColor = 'red';
        loginBtn.disabled = true;
    } else {
        passwordError.style.display = 'none';
        passwordInput.style.borderColor = '#ccc';
        checkFormValidity();
    }
});

function checkFormValidity() {
    const emailPattern = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    if (emailPattern.test(emailInput.value) && passwordInput.value.trim() !== "") {
        loginBtn.disabled = false;
    }
}

document.getElementById('loginForm').addEventListener('submit', function(event) {
    event.preventDefault();

    const credentials = {
        email: emailInput.value,
        password: passwordInput.value
    };

    fetch('http://localhost:8080/api/students/login', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(credentials)
    })
    .then(response => {
        if (response.ok) return response.json();
        return response.json().then(errorData => { throw new Error(errorData.message) });
    })
    .then(data => {
        localStorage.setItem('loggedInStudentId', data.studentId);
        localStorage.setItem('loggedInStudentName', data.name);

        const msgElement = document.getElementById('message');
        msgElement.style.color = "green";
        msgElement.innerText = "Login successful! Redirecting...";

        setTimeout(() => {
            window.location.href = 'dashboard.html';
        }, 1000);
    })
    .catch(error => {
        const msgElement = document.getElementById('message');
        msgElement.style.color = "red";
        msgElement.innerText = error.message;
    });
});