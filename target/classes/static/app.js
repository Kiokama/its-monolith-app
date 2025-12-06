const apiBase = "/api/assessments";
const formSection = document.getElementById("form-section");

document.addEventListener("DOMContentLoaded", () => {
  document.getElementById("refreshBtn").addEventListener("click", loadList);
  document.getElementById("newBtn").addEventListener("click", showForm);
  document.getElementById("assessmentForm").addEventListener("submit", onSave);
  document.getElementById("cancelBtn").addEventListener("click", resetForm);
  loadList();
});

async function loadList() {
  const tbody = document.querySelector("#assessmentsTable tbody");
  tbody.innerHTML =
    '<tr><td colspan="4" class="text-center p-4">...loading</td></tr>';
  try {
    const res = await fetch(apiBase);
    if (!res.ok) {
      throw new Error(`HTTP ${res.status}: ${res.statusText}`);
    }
    const contentType = res.headers.get("content-type");
    if (!contentType || !contentType.includes("application/json")) {
      throw new Error(
        "Response is not JSON. Ensure backend is running on http://localhost:8080"
      );
    }
    const list = await res.json();
    tbody.innerHTML = "";
    list.forEach((a) => {
      const tr = document.createElement("tr");
      tr.className = "border-b hover:bg-gray-50";
      tr.innerHTML = `
                <td class="px-4 py-2">${a.id ?? ""}</td>
                <td class="px-4 py-2">${escapeHtml(a.title)}</td>
                <td class="px-4 py-2">${a.totalPoints ?? ""}</td>
                <td class="px-4 py-2 text-right">
                    <button data-id="${
                      a.id
                    }" class="view text-blue-500 hover:underline mr-2">View</button>
                    <button data-id="${
                      a.id
                    }" class="edit text-indigo-500 hover:underline mr-2">Edit</button>
                    <button data-id="${
                      a.id
                    }" class="delete text-red-500 hover:underline">Delete</button>
                </td>
            `;
      tbody.appendChild(tr);
    });
    attachRowHandlers();
  } catch (err) {
    tbody.innerHTML = `<tr><td colspan="4" class="text-center p-4 text-red-500">Error: ${escapeHtml(
      err.message
    )}</td></tr>`;
    console.error("loadList error:", err);
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
      pre.textContent = `Not found (${res.status})`;
      return;
    }
    const data = await res.json();
    pre.textContent = JSON.stringify(data, null, 2);
  } catch (err) {
    pre.textContent = `Error: ${err.message}`;
    console.error(err);
  }
}

function showForm() {
  resetForm();
  formSection.classList.remove("hidden");
  document.getElementById("formTitle").textContent = "Create Assessment";
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
    formSection.classList.remove("hidden");
  } catch (err) {
    console.error(err);
    alert("Error loading assessment: " + err.message);
  }
}

function resetForm() {
  document.getElementById("assessmentForm").reset();
  document.getElementById("assessmentId").value = "";
  document.getElementById("formTitle").textContent = "Create Assessment";
  formSection.classList.add("hidden");
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
      const contentType = res.headers.get("content-type");
      let errorMsg = `HTTP ${res.status}`;
      if (contentType && contentType.includes("application/json")) {
        try {
          const errorData = await res.json();
          if (errorData.errors) {
            errorMsg = Object.values(errorData.errors).join("\n");
          } else if (errorData.message) {
            errorMsg = errorData.message;
          }
        } catch (e) {
          // ignore parse error
        }
      }
      return alert("Error: " + errorMsg);
    }
    resetForm();
    loadList();
  } catch (err) {
    console.error(err);
    alert("Network error: " + err.message);
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
    alert("Error deleting: " + err.message);
  }
}

function escapeHtml(s = "") {
  if (s === null || s === undefined) {
    return "";
  }
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
