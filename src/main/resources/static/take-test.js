const apiBase = "/api";
let currentAssessmentId = null;
let currentQuestions = [];

document.addEventListener("DOMContentLoaded", () => {
  loadAssessments();
  document
    .getElementById("assessmentSelect")
    .addEventListener("change", loadTest);
  document.getElementById("testForm").addEventListener("submit", submitTest);
  document
    .getElementById("cancelBtn")
    .addEventListener("click", () => (location.href = "index.html"));
});

async function loadAssessments() {
  try {
    const res = await fetch(`${apiBase}/assessments`);
    const assessments = await res.json();
    const select = document.getElementById("assessmentSelect");
    select.innerHTML = '<option value="">-- Choose Assessment --</option>';
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

async function loadTest() {
  currentAssessmentId = document.getElementById("assessmentSelect").value;
  if (!currentAssessmentId) {
    document.getElementById("testContainer").classList.add("hidden");
    return;
  }

  try {
    const res = await fetch(
      `${apiBase}/questions/assessment/${currentAssessmentId}`
    );
    currentQuestions = await res.json();

    const aRes = await fetch(`${apiBase}/assessments/${currentAssessmentId}`);
    const assessment = await aRes.json();

    document.getElementById("testTitle").textContent = assessment.title;
    renderQuestions();
    document.getElementById("testContainer").classList.remove("hidden");
  } catch (err) {
    console.error("Error loading test:", err);
  }
}

function renderQuestions() {
  const container = document.getElementById("questionsList");
  container.innerHTML = "";

  currentQuestions.forEach((q, idx) => {
    const div = document.createElement("div");
    div.className = "border p-4 rounded-md bg-gray-50";

    const questionTitle = document.createElement("p");
    questionTitle.className = "font-bold mb-2";
    questionTitle.textContent = `${idx + 1}. ${escapeHtml(q.content)} (${
      q.points
    } pts)`;
    div.appendChild(questionTitle);

    if (q.questionType === "MCQ") {
      renderMCQ(div, q);
    } else if (q.questionType === "ESSAY") {
      renderEssay(div, q);
    }

    container.appendChild(div);
  });
}

function renderMCQ(div, q) {
  const fieldset = document.createElement("fieldset");
  fieldset.className = "space-y-2";

  // Ensure options is an array
  const options = Array.isArray(q.options) ? q.options : [];

  if (options.length > 0) {
    options.forEach((opt) => {
      const label = document.createElement("label");
      label.className = "flex items-center";

      const input = document.createElement("input");
      input.type = "radio";
      input.name = `question_${q.id}`;
      input.value = opt;
      input.className = "mr-2";

      label.appendChild(input);
      label.appendChild(document.createTextNode(escapeHtml(opt)));
      fieldset.appendChild(label);
    });
  } else {
    const p = document.createElement("p");
    p.textContent = "No options available";
    p.className = "text-gray-500";
    fieldset.appendChild(p);
  }

  div.appendChild(fieldset);
}

function renderEssay(div, q) {
  const textarea = document.createElement("textarea");
  textarea.id = `question_${q.id}`;
  textarea.name = `question_${q.id}`;
  textarea.rows = 5;
  textarea.placeholder = "Enter your answer here...";
  textarea.className = "w-full px-3 py-2 border border-gray-300 rounded-md";
  div.appendChild(textarea);
}

async function submitTest(e) {
  e.preventDefault();

  const studentId = document.getElementById("studentId").value.trim();
  if (!studentId) {
    alert("Please enter your student ID");
    return;
  }

  const answers = [];
  currentQuestions.forEach((q) => {
    let answer = "";
    if (q.questionType === "MCQ") {
      const selected = document.querySelector(
        `input[name="question_${q.id}"]:checked`
      );
      answer = selected ? selected.value : "";
    } else if (q.questionType === "ESSAY") {
      answer = document.getElementById(`question_${q.id}`).value;
    }

    if (answer) {
      answers.push({
        questionId: q.id,
        answer: answer,
      });
    }
  });

  const submissionPayload = {
    studentId: studentId,
    assessmentId: currentAssessmentId,
    answers: answers.map((a) => ({
      questionId: a.questionId,
      answer: a.answer,
    })),
  };

  try {
    const res = await fetch(
      `${apiBase}/assessments/${currentAssessmentId}/submissions`,
      {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(submissionPayload),
      }
    );

    if (res.ok) {
      const submission = await res.json();
      document.getElementById("testContainer").classList.add("hidden");
      document.getElementById(
        "resultScore"
      ).textContent = `Your Score: ${submission.score} points`;
      document.getElementById("resultContainer").classList.remove("hidden");
    } else {
      alert("Error submitting test");
    }
  } catch (err) {
    console.error(err);
    alert("Network error");
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
