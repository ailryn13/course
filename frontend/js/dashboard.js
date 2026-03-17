const studentId = localStorage.getItem('loggedInStudentId');
const studentName = localStorage.getItem('loggedInStudentName');

if (!studentId) {
    alert("Please login first!");
    window.location.href = 'login.html';
} else {
    document.getElementById('welcomeMessage').innerText = "Welcome, " + studentName + "!";
}

function logout() {
    localStorage.clear();

    window.location.href = 'login.html?logout=success';
}