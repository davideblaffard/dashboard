// ---- utilità CSRF (Spring Security) ----
function getCsrf() {
  const token = document.querySelector('meta[name="_csrf"]')?.content || "";
  const header = document.querySelector('meta[name="_csrf_header"]')?.content || "X-CSRF-TOKEN";
  return { token, header };
}

// ---- UI helper ----
function setLoading(isLoading) {
  const area = document.getElementById("content-area");
  if (!area) return;
  if (isLoading) {
    area.innerHTML = `
      <div class="d-flex align-items-center gap-2">
        <div class="spinner-border" role="status" aria-hidden="true"></div>
        <strong>Caricamento…</strong>
      </div>`;
  }
}

// ---- caricamento frammenti (GET/POST) ----
async function loadFragment(url, options = {}) {
  try {
    setLoading(true);

    const { method = "GET", body = null, headers = {} } = options;
    const resp = await fetch(url, { method, body, headers });
    if (!resp.ok) throw new Error(`HTTP ${resp.status}`);
    const html = await resp.text();

    const area = document.getElementById("content-area");
    if (area) area.innerHTML = html;

    // ri-aggancia i listener dopo che il DOM del fragment è stato sostituito
    bindInsideContent();
  } catch (err) {
    const area = document.getElementById("content-area");
    if (area) {
      area.innerHTML = `<div class="alert alert-danger">Errore nel caricamento della sezione.</div>`;
    }
    console.error(err);
  }
}

// ---- intercetta click nel menu sinistro (link con .js-nav) ----
function bindSidebar() {
  document.querySelectorAll(".js-nav").forEach((a) => {
    a.addEventListener("click", (e) => {
      // lascia passare il logout
      if (a.classList.contains("text-danger")) return;
      e.preventDefault();
      const url = a.getAttribute("href");
      if (url) loadFragment(url);
    });
  });
}

// ---- intercetta link & form *dentro* il content (delegation) ----
function bindInsideContent() {
  const area = document.getElementById("content-area");
  if (!area) return;

  // Delegation per <a> interni: carica via fetch se relativo alla dashboard
  area.querySelectorAll("a").forEach((a) => {
    // escludi link esterni/ancore/mailto/download
    const href = a.getAttribute("href") || "";
    const isAction = a.getAttribute("data-ajax") === "true" || href.startsWith("/") || href.startsWith("./");
    const isDownload = a.hasAttribute("download") || a.target === "_blank" || a.hasAttribute("data-download");
    if (!href || !isAction || isDownload) return;

    a.addEventListener("click", (e) => {
      e.preventDefault();
      loadFragment(href);
    });
  });

  // Delegation per <form> interni: POST via fetch (anche multipart)
  area.querySelectorAll("form").forEach((form) => {
    form.addEventListener("submit", async (e) => {
      e.preventDefault();
      const action = form.getAttribute("action") || window.location.pathname;
      const method = (form.getAttribute("method") || "post").toUpperCase();
      const { token, header } = getCsrf();

      let body;
      let headers = {};
      // Usa FormData sempre (gestisce anche multipart+file)
      body = new FormData(form);

      // Aggiungi CSRF nel body (comunque) e come header (per sicurezza)
      if (token) {
        if (!body.has("_csrf")) body.append("_csrf", token);
        headers[header] = token;
      }

      await loadFragment(action, { method, body, headers });
    });
  });
}

// ---- opzionale: carica una sezione di default all’avvio ----
function loadDefaultSection() {
  // cambia qui se vuoi aprire automaticamente suppliers o altro
  // loadFragment('/suppliers');
}

document.addEventListener("DOMContentLoaded", () => {
  bindSidebar();
  bindInsideContent();
  loadDefaultSection();
});

// Esponi la funzione se vuoi usarla inline: onclick="loadFragment('/suppliers')"
window.loadFragment = loadFragment;
