document.getElementById('csvUploadForm').onsubmit = function(event) {
    event.preventDefault();
    const formData = new FormData(this);
    const jobDetailsElement = document.getElementById('jobDetails');

    // Create a temporary job entry to display immediately
    const tempJobId = 'Processing-' + new Date().getTime(); // Generate a temporary unique ID
    const tempJobElement = document.createElement('div');
    tempJobElement.innerHTML = `
        <div class="job-entry">
            <p>Job ID: <span>${tempJobId}</span></p>
            <p>Date Created: <span>${new Date().toLocaleString()}</span></p>
            <p>Status: <span>PROCESSING</span></p>
        </div>
    `;
    jobDetailsElement.insertBefore(tempJobElement, jobDetailsElement.firstChild);

    // Submit the form data
    fetch('/upload-csv', {
        method: 'POST',
        body: formData,
    })
        .then(response => response.json())
        .then(job => {
            // Remove the temporary job entry
            tempJobElement.remove();

            // Create a new job entry with the actual data received from the backend
            const jobElement = document.createElement('div');
            jobElement.innerHTML = `
            <div class="job-entry">
                <p>Job ID: <span>${job.jobID}</span></p>
                <p>Date Created: <span>${job.dateCreated}</span></p>
                <p>Status: <span>${job.currentStatus}</span></p>
                <button onclick="toggleDetails(this, '${job.jobID}')">View</button>
                <div class="filtered-data" style="display: none;">
                    ${formatPatentData(job.filteredData)}
                </div>
            </div>
        `;
            jobDetailsElement.insertBefore(jobElement, jobDetailsElement.firstChild);
        })
        .catch(error => {
            console.error('Error:', error);
            jobDetailsElement.textContent = 'Error processing the file.';
        });
};


function formatPatentData(filteredData) {
    return filteredData.map(data => `
        <div class="patent-data-container">
       ${data["Application Number"] ? `<span><strong>Application Number:</strong> ${data["Application Number"]}</span>&emsp;&emsp;` : ''}
            ${data["Publication Date"] ? `<span><strong>Publication Date:</strong> ${data["Publication Date"]}</span>&emsp;&emsp;` : ''}
            ${data["Applicants"] ? `<span><strong>Applicants:</strong> ${data["Applicants"]}</span>` : ''}
            <br/>
            <p><strong>Title:</strong> ${data["Title"] || 'N/A'}</p>
            <p><strong>Abstract:</strong> ${data["Abstract"] || 'N/A'}</p>
            <p><strong>Inventors:</strong> ${data["Inventors"] ? data["Inventors"].split(",").join(", ") : 'N/A'}</p>
            ${data["LinkedIn URL 1"] ? `<p><strong>LinkedIn URL 1:</strong> <a href="${data["LinkedIn URL 1"]}" target="_blank" rel="noopener noreferrer">${data["LinkedIn URL 1"]}</a>a</p>` : ''}
            ${data["LinkedIn URL 2"] ? `<p><strong>LinkedIn URL 2:</strong> <a href="${data["LinkedIn URL 2"]}" target="_blank" rel="noopener noreferrer">${data["LinkedIn URL 2"]}</a></p>` : ''}
            ${data["Website URL"] ? `<p><strong>Website URL:</strong> <a href="${data["Website URL"]}" target="_blank" rel="noopener noreferrer">${data["Website URL"]}</a></p>` : ''}
           <p><strong>Email Address:</strong> ${data["Email Address"] && data["Email Address"] !== '' ? `${data["Email Address"]}` : 'Email Not Found in the Website'}</p>
        </div>
        <hr>
    `).join('');
}



function toggleDetails(button, jobId) {
    const filteredDataElement = button.nextElementSibling;
    if (filteredDataElement.style.display === 'none') {
        filteredDataElement.style.display = 'block';
        button.textContent = 'Back to Jobs List';
        // Hide other jobs except for the one being viewed
        document.querySelectorAll('.job-entry').forEach(entry => {
            if (!entry.innerHTML.includes(jobId)) {
                entry.style.display = 'none';
            }
        });
    } else {
        filteredDataElement.style.display = 'none';
        button.textContent = 'View';
        // Show all jobs again
        document.querySelectorAll('.job-entry').forEach(entry => {
            entry.style.display = '';
        });
    }
}
function fetchAndDisplayAllJobs() {
    fetch('/all')
        .then(response => response.json())
        .then(jobs => {
            const jobDetailsElement = document.getElementById('jobDetails');
            jobs.forEach(job => {
                const jobElement = document.createElement('div');
                jobElement.classList.add("job-entry"); // Ensure this class matches your CSS for styling
                jobElement.innerHTML = `
                    <p>Job ID: <span>${job.jobID}</span></p>
                    <p>Date Created: <span>${job.dateCreated}</span></p> <!-- Adjust according to your date formatting -->
                    <p>Status: <span>${job.currentStatus}</span></p>
                    <button onclick="toggleDetails(this)">Show More</button>
                    <div class="filtered-data" style="display: none;">
                        ${JSON.stringify(job.filteredData, null, 2)}
                    </div>
                `;
                // Append each job to the jobDetails container
                jobDetailsElement.appendChild(jobElement);
            });
        })
        .catch(error => {
            console.error('Error:', error);
            jobDetailsElement.textContent = 'Error fetching job details.';
        });
}

document.addEventListener('DOMContentLoaded', fetchAndDisplayAllJobs);
