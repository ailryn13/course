const studentId = localStorage.getItem('loggedInStudentId');

if (!studentId) {
    alert("Please login first!");
    window.location.href = 'login.html';
}

window.onload = function() {
    fetchCourses();
};

function fetchCourses() {
    // Fetch both courses and the student's current enrollments at the same time
    Promise.all([
        fetch('http://localhost:8080/api/courses').then(res => res.json()),
        fetch(`http://localhost:8080/api/students/${studentId}/enrollments`).then(res => res.json())
    ])
    .then(([courses, enrollments]) => {
        // Extract just the course IDs that the student is enrolled in for easy checking
        const enrolledCourseIds = enrollments.map(enrollment => enrollment.course.id);

        const tbody = document.getElementById('courseTableBody');
        tbody.innerHTML = '';

        courses.forEach(course => {
            const isFull = course.availableSeats <= 0;
            const isEnrolled = enrolledCourseIds.includes(course.id);

            // Determine what the button should look like based on status
            let buttonHtml = '';
            if (isEnrolled) {
                // Already enrolled
                buttonHtml = `<button class="success-btn" disabled>Enrolled</button>`;
            } else if (isFull) {
                // Not enrolled, but course is full
                buttonHtml = `<button class="success-btn" disabled>Full</button>`;
            } else {
                // Available to enroll
                buttonHtml = `<button class="success-btn" onclick="enrollCourse(${course.id})">Enroll</button>`;
            }

            const row = `<tr>
                <td>${course.id}</td>
                <td>${course.name}</td>
                <td>${course.description}</td>
                <td>${course.duration}</td>
                <td>${course.availableSeats}</td>
                <td>${buttonHtml}</td>
            </tr>`;
            tbody.innerHTML += row;
        });
    })
    .catch(error => console.error('Error fetching data:', error));
}

function showAlert(title, message) {
    document.getElementById('alertTitle').innerText = title;
    document.getElementById('alertMessage').innerText = message;
    document.getElementById('alertModal').style.display = 'flex';
}

function closeAlertModal() {
    document.getElementById('alertModal').style.display = 'none';
}

function enrollCourse(courseId) {
    const msgElement = document.getElementById('message');
    msgElement.style.color = "blue";
    msgElement.innerText = "Enrolling...";

    fetch(`http://localhost:8080/api/enroll/${courseId}?studentId=${studentId}`, { method: 'POST' })
    .then(response => {
        if (response.ok) {
            showAlert("Success!", "Successfully enrolled in the course!");
            fetchCourses(); // Refreshes the table, changing the button to "Enrolled"
            msgElement.innerText = "";
        } else {
            return response.text().then(text => { throw new Error(text) });
        }
    })
    .catch(error => {
        showAlert("Enrollment Failed", error.message);
        msgElement.innerText = "";
    });
}