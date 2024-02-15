// Dynamically generate filter options based on your CSV fields
const filterOptions = [
    { id: 'applicationNumber', label: 'Application Number' },
    { id: 'title', label: 'Title' },
    { id: 'publicationDate', label: 'Publication Date' },
    { id: 'abstract', label: 'Abstract' },
    { id: 'applicants', label: 'Applicants' },
    { id: 'inventors', label: 'Inventors' }
];

const filterContainer = document.getElementById('filterOptions');
filterOptions.forEach(option => {
    const input = document.createElement('input');
    input.type = 'checkbox';
    input.id = option.id;
    input.name = 'fields';
    input.value = option.label;

    const label = document.createElement('label');
    label.htmlFor = option.id;
    label.textContent = option.label;

    const div = document.createElement('div');
    div.appendChild(input);
    div.appendChild(label);
    filterContainer.appendChild(div);
});

document.getElementById('csvUploadForm').onsubmit = function(event) {
    event.preventDefault();
    const fileInput = document.getElementById('file');
    const file = fileInput.files[0];
    const selectedFilters = [...filterContainer.querySelectorAll('input[type="checkbox"]:checked')].map(cb => cb.value);

    let formData = new FormData();
    formData.append('file', file);
    selectedFilters.forEach(filter => formData.append('fields', filter));

    fetch('/upload-csv', {
        method: 'POST',
        body: formData
    })
    .then(response => response.json())
    .then(data => {
        addJobToList(data); // Adjusted to add the job to the list
    })
    .catch(error => {
        console.error('Error:', error);
    });
};

function addJobToList(job) {
    const jobDetailsContainer = document.getElementById('jobDetails');
    const jobCard = document.createElement('div');
    jobCard.classList.add('job-card');
    jobCard.innerHTML = `
        <div class="job-details">
            <p><strong>Job ID:</strong> ${job.jobID}</p>
            <p><strong>Created:</strong> ${job.dateCreated}</p>
            <p><strong>Status:</strong> ${job.status}</p>
        </div>
        <button class="view-button" onclick="viewJobDetails('${job.jobID}')">View</button>
    `;
    jobDetailsContainer.appendChild(jobCard);
}

document.getElementById('showAllJobsButton').addEventListener('click', showAllJobs);

function showAllJobs() {
    fetch('/all')
    .then(response => response.json())
    .then(jobs => {
        const jobsListContainer = document.getElementById('jobDetails');
        jobsListContainer.innerHTML = ''; // Clear existing jobs
        jobs.forEach(job => {
            addJobToList(job); // Use this function to create and append job cards
        });
    })
    .catch(error => {
        console.error('Error:', error);
    });
}

window.viewJobDetails = function(jobID) {
    // Fetch job details and display logic here
    // For demonstration, using static content
    const jobDetailsContainer = document.getElementById('jobDetails');
    jobDetailsContainer.innerHTML = ''; // Clear existing content

    const jobDetailsDiv = document.createElement('div');
    jobDetailsDiv.innerHTML = `<p>Details for Job ID: ${jobID}</p>`;
    jobDetailsContainer.appendChild(jobDetailsDiv);

    // Back button to return to job list
    const backButton = document.createElement('button');
    backButton.textContent = 'Back to Jobs List';
    backButton.classList.add('view-button');
    backButton.onclick = showAllJobs; // Reuse the showAllJobs function to go back
    jobDetailsContainer.appendChild(backButton);
};

// Call showAllJobs on initial load to populate the job list
showAllJobs();
