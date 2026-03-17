// --- 1. INITIALIZATION & AUTHENTICATION ---
const studentId = localStorage.getItem('loggedInStudentId');

// Redirect to login if the student ID is not found in local storage
if (!studentId) {
    alert("Please login first!");
    window.location.href = 'login.html';
}

// Load enrollments as soon as the page is ready
window.onload = function() {
    fetchEnrollments();
};

// --- 2. FETCH & DISPLAY ENROLLMENTS ---
function fetchEnrollments() {
    fetch(`http://localhost:8080/api/students/${studentId}/enrollments`)
        .then(response => response.json())
        .then(enrollments => {
            const tbody = document.getElementById('enrollmentTableBody');
            tbody.innerHTML = '';

            // Handle the case where the student has no courses
            if (enrollments.length === 0) {
                tbody.innerHTML = '<tr><td colspan="4" style="text-align: center;">You are not enrolled in any courses yet.</td></tr>';
                return;
            }

            // Populate the table with enrolled courses
            enrollments.forEach(enrollment => {
                const row = `<tr>
                    <td>${enrollment.id}</td>
                    <td>${enrollment.course.name}</td>
                    <td>${enrollment.course.duration}</td>
                    <td><button class="danger-btn" onclick="unenrollCourse(${enrollment.course.id})">Unenroll</button></td>
                </tr>`;
                tbody.innerHTML += row;
            });
        })
        .catch(error => console.error('Error fetching enrollments:', error));
}

// --- 3. CUSTOM MODAL LOGIC FOR UNENROLLMENT ---

let courseToDrop = null; // Variable to temporarily remember the course ID

// Triggered by the "Unenroll" button in the table
function unenrollCourse(courseId) {
    courseToDrop = courseId; // Save the ID
    document.getElementById('customConfirmModal').style.display = 'flex'; // Show the Confirm popup
}

// Triggered by the "Cancel" button in the popup
function closeConfirmModal() {
    document.getElementById('customConfirmModal').style.display = 'none'; // Hide the popup
    courseToDrop = null; // Clear the memory
}

// Triggered by the "Yes, Drop It" button in the popup
function executeUnenroll() {
    // Save the course ID to a local variable before clearing the global one
    const idToDrop = courseToDrop;

    // Hide the confirm popup and clear the global variable
    closeConfirmModal();

    const msgElement = document.getElementById('message');
    if(msgElement) {
        msgElement.style.color = "blue";
        msgElement.innerText = "Processing...";
    }

    // Use our new 'idToDrop' variable in the fetch URL
    fetch(`http://localhost:8080/api/enroll/${idToDrop}?studentId=${studentId}`, { method: 'DELETE' })
    .then(response => {
        if (response.ok) {
            showAlert("Success", "Successfully unenrolled!");
            fetchEnrollments(); // Refresh the table so the dropped course disappears
            if(msgElement) msgElement.innerText = "";
        } else {
            return response.text().then(text => { throw new Error(text) });
        }
    })
    .catch(error => {
        showAlert("Unenrollment Failed", error.message);
        if(msgElement) msgElement.innerText = "";
    });
}

// --- 4. CUSTOM ALERT MODAL HELPER FUNCTIONS ---

// Shows custom alert (replaces the default browser alert())
function showAlert(title, message) {
    document.getElementById('alertTitle').innerText = title;
    document.getElementById('alertMessage').innerText = message;
    document.getElementById('customAlertModal').style.display = 'flex';
}

// Closes the custom alert modal
function closeAlertModal() {
    document.getElementById('customAlertModal').style.display = 'none';
}