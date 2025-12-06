const apiBase = "/api";

document.addEventListener("DOMContentLoaded", () => {
  loadAssessments();
  document
    .getElementById("questionType")
    .addEventListener("change", toggleQuestionTypeFields);
  document
    .getElementById("assessmentSelect")
    .addEventListener("change", loadQuestions);
  document
    .getElementById("questionForm")
    .addEventListener("submit", onSaveQuestion);
  toggleQuestionTypeFields();
});

async function loadAssessments() {
  try {
    const res = await fetch(`${apiBase}/assessments`);
    const assessments = await res.json();
    const select = document.getElementById("assessmentSelect");
    select.innerHTML = '<option value="">-- Select Assessment --</option>';
    assessments.forEach((a) => {
      const opt = document.createElement("option");
      opt.value = a.id;
      opt.textContent = `${a.title} (${a.totalPoints} pts)`;
      select.appendChild(opt);
    });
  } catch (err) {
    console.error("Error loading assessments:", err);
  }
}

function toggleQuestionTypeFields() {
  const type = document.getElementById("questionType").value;
  document
    .getElementById("mcqSection")
    .classList.toggle("hidden", type !== "MCQ");
  document
    .getElementById("essaySection")
    .classList.toggle("hidden", type !== "ESSAY");
}

async function loadQuestions() {
  const assessmentId = document.getElementById("assessmentSelect").value;
  if (!assessmentId) {
    document.getElementById("previewList").innerHTML =
      '<p class="text-gray-500">Select an assessment</p>';
    return;
  }

  try {
    const res = await fetch(`${apiBase}/questions/assessment/${assessmentId}`);
    const questions = await res.json();
    const list = document.getElementById("previewList");
    list.innerHTML = "";
    questions.forEach((q) => {
      const div = document.createElement("div");
      div.className = "border p-4 rounded-md bg-gray-50";
      let optionsStr = "";
      if (q.questionType === "MCQ" && q.options) {
        optionsStr = `<p class="text-sm text-gray-600">Options: ${
          Array.isArray(q.options) ? q.options.join(", ") : q.options
        }</p>`;
      }
      div.innerHTML = `
                <p class="font-bold">${escapeHtml(q.content)}</p>
                <p class="text-sm text-gray-600">Type: ${
                  q.questionType
                }, Points: ${q.points}</p>
                ${optionsStr}
                <button onclick="deleteQuestion(${
                  q.id
                })" class="text-red-500 hover:underline text-sm mt-2">Delete</button>
            `;
      list.appendChild(div);
    });
  } catch (err) {
    console.error("Error loading questions:", err);
  }
}

async function onSaveQuestion(e) {
  e.preventDefault();
  const assessmentId = document.getElementById("assessmentSelect").value;
  if (!assessmentId) {
    alert("Please select an assessment");
    return;
  }

  const type = document.getElementById("questionType").value;
  const payload = {
    content: document.getElementById("content").value,
    points: parseInt(document.getElementById("points").value),
    assessmentId: parseInt(assessmentId),
    questionType: type,
  };

  if (type === "MCQ") {
    const optionsStr = document.getElementById("optionsInput").value;
    // Split comma-separated options into array
    payload.options = optionsStr
      .split(",")
      .map((o) => o.trim())
      .filter((o) => o.length > 0);
    payload.correctAnswer = document.getElementById("correctAnswer").value;
  } else if (type === "ESSAY") {
    payload.rubric = document.getElementById("rubric").value;
  }

  try {
    const res = await fetch(`${apiBase}/questions`, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(payload),
    });
    if (res.ok) {
      document.getElementById("questionForm").reset();
      loadQuestions();
      alert("Question saved!");
    } else {
      const err = await res.json();
      alert("Error: " + (err.message || res.status));
    }
  } catch (err) {
    console.error(err);
    alert("Network error");
  }
}

async function deleteQuestion(id) {
  if (!confirm("Delete this question?")) return;
  try {
    const res = await fetch(`${apiBase}/questions/${id}`, { method: "DELETE" });
    if (res.ok) {
      loadQuestions();
    } else {
      alert("Delete failed");
    }
  } catch (err) {
    console.error(err);
  }
}

function escapeHtml(s = "") {
  if (!s) return "";
  return String(s).replace(
    /[&<>"']/g,
    (c) =>
      ({
        "&": "&amp;",
        "<": "&lt;",
        ">": "&gt;",
        '"': "&quot;",
        "'": "&#39;",
      }[c])
  );
}
