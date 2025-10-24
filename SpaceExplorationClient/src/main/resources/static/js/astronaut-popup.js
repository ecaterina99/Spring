function showPopup(button) {
    let astronautButtons = [];
    let currentAstronautIndex = -1;

    astronautButtons = Array.from(document.querySelectorAll(".btn-view-astronauts"));
    currentAstronautIndex = astronautButtons.indexOf(button);

    document.getElementById("prevMissionBtn").onclick = () => {
        if (currentAstronautIndex > 0) showPopup(astronautButtons[currentAstronautIndex - 1]);
    };
    document.getElementById("nextMissionBtn").onclick = () => {
        if (currentAstronautIndex < astronautButtons.length - 1) showPopup(astronautButtons[currentAstronautIndex + 1]);
    };

    const firstname = button.getAttribute("data-firstname");
    const lastname = button.getAttribute("data-lastname");
    const specialization = button.getAttribute("data-specialization");
    const image = button.getAttribute("data-image");
    const experience = button.getAttribute("data-experience");
    const birthDate = button.getAttribute("data-birthdate");
    const health = button.getAttribute("data-health");
    const fitness = button.getAttribute("data-fitness");
    const education = button.getAttribute("data-education");
    const psychological = button.getAttribute("data-psychological");
    const overall = button.getAttribute("data-overall");
    const rate = button.getAttribute("data-rate");

    const img = document.getElementById("astronaut-img");
    img.src = image || "";

    document.getElementById("popup-name").textContent = `${firstname || ""} ${lastname || ""}`.trim();
    document.getElementById("popup-specialization").textContent = specialization || "—";
    document.getElementById("popup-birthDate").textContent = birthDate || "—";
    document.getElementById("popup-experience").textContent = experience || "0";
    document.getElementById("popup-status").textContent = health || "—";
    document.getElementById("popup-fitness").textContent = fitness || "—";
    document.getElementById("popup-education").textContent = education || "—";
    document.getElementById("popup-psychological").textContent = psychological || "—";
    document.getElementById("popup-overall").textContent = overall || "—";
    document.getElementById("popup-rate").textContent = rate || "—";

    const popup = document.getElementById("astronaut-popup");
    popup.style.display = "flex";

    document.getElementById("popup-close").onclick = () => popup.style.display = "none";
    popup.onclick = (e) => {
        if (e.target.id === "astronaut-popup") popup.style.display = "none";
    };
    document.onkeydown = (e) => {
        if (e.key === "Escape") popup.style.display = "none";
    };
}