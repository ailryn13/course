const nameInput = document.getElementById('name');
const emailInput = document.getElementById('email');
const passwordInput = document.getElementById('password');
const nameError = document.getElementById('nameError'); // Grab the new name error element
const emailError = document.getElementById('emailError');
const passwordError = document.getElementById('passwordError');
const registerBtn = document.getElementById('registerBtn');

// Real-time Name Validation
nameInput.addEventListener('input', function() {
    // Regex allows uppercase, lowercase, and spaces
    const namePattern = /^[A-Za-z ]+$/;

    if (nameInput.value.length > 0 && !namePattern.test(nameInput.value)) {
        nameError.style.display = 'block';
        nameInput.style.borderColor = 'red';
        registerBtn.disabled = true;
    } else {
        nameError.style.display = 'none';
        nameInput.style.borderColor = '#ccc';
        checkFormValidity();
    }
});

// Real-time Email Validation
emailInput.addEventListener('input', function() {
    const emailPattern = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;

    if (emailInput.value.length > 0 && !emailPattern.test(emailInput.value)) {
        emailError.style.display = 'block';
        emailInput.style.borderColor = 'red';
        registerBtn.disabled = true;
    } else {
        emailError.style.display = 'none';
        emailInput.style.borderColor = '#ccc';
        checkFormValidity();
    }
});

// Real-time Password Validation
passwordInput.addEventListener('input', function() {
    if (passwordInput.value.length > 0 && passwordInput.value.length < 6) {
        passwordError.style.display = 'block';
        passwordInput.style.borderColor = 'red';
        registerBtn.disabled = true;
    } else {
        passwordError.style.display = 'none';
        passwordInput.style.borderColor = '#ccc';
        checkFormValidity();
    }
});

// Helper function to enable/disable button based on overall validity
function checkFormValidity() {
    const emailPattern = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    const namePattern = /^[A-Za-z ]+$/;

    const isEmailValid = emailPattern.test(emailInput.value);
    const isPasswordValid = passwordInput.value.length >= 6;
    const isNameValid = namePattern.test(nameInput.value) && nameInput.value.trim() !== "";

    if (isEmailValid && isPasswordValid && isNameValid) {
        registerBtn.disabled = false;
    } else {
        registerBtn.disabled = true;
    }
}

// Original Form Submission Logic
document.getElementById('registerForm').addEventListener('submit', function(event) {
    event.preventDefault();

    const name = nameInput.value;
    const email = emailInput.value;
    const password = passwordInput.value;

    const studentData = { name: name, email: email, password: password };

    fetch('http://localhost:8080/api/students/register', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(studentData)
    })
    .then(response => {
        if (response.ok) return response.json();
        return response.json().then(errorData => { throw new Error(errorData.message) });
    })
    .then(data => {
        const msgElement = document.getElementById('message');
        msgElement.style.color = "green";
        msgElement.innerText = "Registration successful! Redirecting to login...";

        setTimeout(() => {
            window.location.href = 'login.html';
        }, 2000);
    })
    .catch(error => {
        const msgElement = document.getElementById('message');
        msgElement.style.color = "red";
        msgElement.innerText = error.message;
    });
});