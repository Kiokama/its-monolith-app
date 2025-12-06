const apiBase = "/api/assessments";

document.addEventListener("DOMContentLoaded", () => {
  document.getElementById("refreshBtn").addEventListener("click", loadList);
  document.getElementById("assessmentForm").addEventListener("submit", onSave);
  document.getElementById("cancelBtn").addEventListener("click", resetForm);
  loadList();
});

async function loadList() {
  const tbody = document.querySelector("#assessmentsTable tbody");
  tbody.innerHTML = "...loading";
  try {
    const res = await fetch(apiBase);
    const list = await res.json();
    tbody.innerHTML = "";
    list.forEach((a) => {
      const tr = document.createElement("tr");
      tr.innerHTML = `
				<td>${a.id ?? ""}</td>
				<td>${escapeHtml(a.title)}</td>
				<td>${a.totalPoints ?? ""}</td>
				<td>
					<button data-id="${a.id}" class="view">View</button>
					<button data-id="${a.id}" class="edit">Edit</button>
					<button data-id="${a.id}" class="delete">Delete</button>
				</td>
			`;
      tbody.appendChild(tr);
    });
    attachRowHandlers();
  } catch (err) {
    tbody.innerHTML = '<tr><td colspan="4">Error loading list</td></tr>';
    console.error(err);
  }
}

function attachRowHandlers() {
  document
    .querySelectorAll("#assessmentsTable .view")
    .forEach((btn) =>
      btn.addEventListener("click", (e) => viewAssessment(e.target.dataset.id))
    );
  document
    .querySelectorAll("#assessmentsTable .edit")
    .forEach((btn) =>
      btn.addEventListener("click", (e) => editAssessment(e.target.dataset.id))
    );
  document
    .querySelectorAll("#assessmentsTable .delete")
    .forEach((btn) =>
      btn.addEventListener("click", (e) =>
        deleteAssessment(e.target.dataset.id)
      )
    );
}

async function viewAssessment(id) {
  const pre = document.getElementById("detailPre");
  pre.textContent = "loading...";
  try {
    const res = await fetch(`${apiBase}/${id}`);
    if (!res.ok) {
      pre.textContent = "Not found";
      return;
    }
    const data = await res.json();
    pre.textContent = JSON.stringify(data, null, 2);
  } catch (err) {
    pre.textContent = "Error";
    console.error(err);
  }
}

async function editAssessment(id) {
  try {
    const res = await fetch(`${apiBase}/${id}`);
    if (!res.ok) return alert("Assessment not found");
    const a = await res.json();
    document.getElementById("assessmentId").value = a.id;
    document.getElementById("title").value = a.title || "";
    document.getElementById("description").value = a.description || "";
    document.getElementById("totalPoints").value = a.totalPoints ?? 0;
    document.getElementById("formTitle").textContent = "Edit Assessment";
  } catch (err) {
    console.error(err);
  }
}

function resetForm() {
  document.getElementById("assessmentId").value = "";
  document.getElementById("title").value = "";
  document.getElementById("description").value = "";
  document.getElementById("totalPoints").value = 0;
  document.getElementById("formTitle").textContent = "Create Assessment";
}

async function onSave(e) {
  e.preventDefault();
  const id = document.getElementById("assessmentId").value;
  const payload = {
    title: document.getElementById("title").value,
    description: document.getElementById("description").value,
    totalPoints: parseInt(document.getElementById("totalPoints").value) || 0,
  };
  try {
    let res;
    if (id) {
      res = await fetch(`${apiBase}/${id}`, {
        method: "PUT",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(payload),
      });
    } else {
      res = await fetch(apiBase, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(payload),
      });
    }
    if (!res.ok) {
      const text = await res.text();
      return alert("Error: " + res.status + " " + text);
    }
    resetForm();
    loadList();
  } catch (err) {
    console.error(err);
    alert("Network error");
  }
}

async function deleteAssessment(id) {
  if (!confirm("Delete assessment #" + id + "?")) return;
  try {
    const res = await fetch(`${apiBase}/${id}`, { method: "DELETE" });
    if (res.status === 204) {
      loadList();
      document.getElementById("detailPre").textContent = "Deleted";
    } else {
      alert("Delete failed: " + res.status);
    }
  } catch (err) {
    console.error(err);
  }
}

function escapeHtml(s = "") {
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
