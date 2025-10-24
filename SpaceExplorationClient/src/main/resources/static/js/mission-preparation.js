document.addEventListener('DOMContentLoaded', () => {
    const accessToken = window.MISSION_CONFIG?.accessToken || '';
    const userId = window.MISSION_CONFIG?.userId || '';
    const storedUserId = sessionStorage.getItem('currentUserId');

    if (storedUserId && storedUserId !== userId.toString()) {
        console.log('Different user detected, clearing session data');
        Object.keys(sessionStorage).forEach(key => {
            if (key.startsWith('user_')) {
                sessionStorage.removeItem(key);
            }
        });
    }

    if (userId) {
        sessionStorage.setItem('currentUserId', userId);
    }
    if (accessToken) {
        sessionStorage.setItem('accessToken', accessToken);
    }
    if (!sessionStorage.getItem('allMissionCrews')) {
        sessionStorage.setItem('allMissionCrews', JSON.stringify({}));
    }
    initializeUserMissionData(userId);
    const missionId = document.querySelector('.btn-add-participants')?.getAttribute('data-mission-id');
    if (missionId) {
        checkAndClearStartedMission(missionId);
    }
    displayCurrentCrew();
    displayCrewMembers();
});

document.getElementById('scrollLeft').addEventListener('click', function () {
    const container = document.getElementById('astronautsScroll');
    container.scrollBy({left: -250, behavior: 'smooth'});
});

document.getElementById('scrollRight').addEventListener('click', function () {
    const container = document.getElementById('astronautsScroll');
    container.scrollBy({left: 250, behavior: 'smooth'});
});


function showAlert(message, type = 'danger') {
    const container = document.getElementById('alert-container');
    const alert = document.createElement('div');
    alert.className = `alert alert-${type} alert-dismissible fade show shadow`;
    alert.role = 'alert';
    alert.innerHTML = `
        <strong>${type === 'success' ? 'Success!' : ''}</strong> ${message}
    `;
    container.appendChild(alert);
    setTimeout(() => alert.remove(), 2000);
}

function checkAndClearStartedMission(missionId) {
    const startedMissionsKey = 'startedMissions';
    let startedMissions = JSON.parse(sessionStorage.getItem(startedMissionsKey) || '[]');

    if (startedMissions.includes(missionId)) {
        console.log(`Mission ${missionId} was already started, clearing crew data`);
        let userMissions = getUserMissionData();
        delete userMissions[`mission_${missionId}_crew`];
        saveUserMissionData(userMissions);
    }
}

function initializeUserMissionData(userId) {
    const userDataKey = `user_${userId}_missions`;
    if (!sessionStorage.getItem(userDataKey)) {
        sessionStorage.setItem(userDataKey, JSON.stringify({}));
    }
}

function getUserMissionData() {
    const userId = sessionStorage.getItem('currentUserId');
    const userDataKey = `user_${userId}_missions`;
    return JSON.parse(sessionStorage.getItem(userDataKey) || '{}');
}

function saveUserMissionData(data) {
    const userId = sessionStorage.getItem('currentUserId');
    const userDataKey = `user_${userId}_missions`;
    sessionStorage.setItem(userDataKey, JSON.stringify(data));
}

document.querySelectorAll(".btn-add-participants").forEach(btn => {
    btn.addEventListener("click", () => {
        const astronautId = btn.getAttribute("data-astronaut-id");
        const missionId = btn.getAttribute("data-mission-id");
        const crewSize = btn.getAttribute("data-crew-size");

        let userMissions = getUserMissionData();
        let currentMissionCrew = userMissions[`mission_${missionId}_crew`] || [];

        if (currentMissionCrew.some(member => member.astronautId === astronautId)) {
            showAlert("This astronaut is already in the crew!", "warning");
            return;
        }

        if (currentMissionCrew.length >= crewSize) {
            showAlert("Mission crew is full!", "warning");
            return;
        }

        const viewBtn = btn.previousElementSibling;

        currentMissionCrew.push({
            astronautId: astronautId,
            missionId: missionId,
            firstName: viewBtn.getAttribute('data-firstname') || '',
            lastName: viewBtn.getAttribute('data-lastname') || '',
            specialization: viewBtn.getAttribute('data-specialization') || '',
            overallScore: viewBtn.getAttribute('data-overall') || '0',
            healthStatus: viewBtn.getAttribute('data-health') || 'UNKNOWN'
        });

        userMissions[`mission_${missionId}_crew`] = currentMissionCrew;
        saveUserMissionData(userMissions);

        btn.textContent = "ADDED ✓";
        btn.disabled = true;
        btn.classList.add('btn-success');

        displayCurrentCrew();
        displayCrewMembers();
    });
});

function displayCurrentCrew() {
    const missionId = document.querySelector('.btn-add-participants')?.getAttribute('data-mission-id');
    if (!missionId) {
        console.warn('Mission ID not found');
        return;
    }

    const currentMissionCrew = getMissionCrew(missionId);

    const crewCount = document.getElementById('crew-count');
    if (crewCount) {
        crewCount.textContent = `Crew members: ${currentMissionCrew.length}`;
    }

    document.querySelectorAll(".btn-add-participants").forEach(btn => {
        const astronautId = btn.getAttribute("data-astronaut-id");
        const isAdded = currentMissionCrew.some(member => member.astronautId === astronautId);

        if (isAdded) {
            btn.textContent = "ADDED ✓";
            btn.disabled = true;
            btn.classList.add('btn-success');
        } else {
            btn.textContent = "ADD TO CREW";
            btn.disabled = false;
            btn.classList.remove('btn-success');
        }
    });
}

function displayCrewMembers() {
    const missionId = document.querySelector('.btn-add-participants')?.getAttribute('data-mission-id');
    if (!missionId) return;

    const crew = getMissionCrew(missionId);
    const participantsDiv = document.querySelector('.participants');
    const clearBtnContainer = document.getElementById('clear-crew-btn-container');

    let emptyMessageDiv = participantsDiv.querySelector('div[th\\:if]') ||
        participantsDiv.querySelector('.lead')?.parentElement;
    let crewCardsContainer = participantsDiv.querySelector('.added-crew-card');

    if (crew.length === 0) {
        if (crewCardsContainer) {
            crewCardsContainer.remove();
        }

        if (emptyMessageDiv) {
            emptyMessageDiv.style.display = 'block';
        }

        if (clearBtnContainer) {
            clearBtnContainer.style.display = 'none';
        }
        return;
    }

    if (emptyMessageDiv) {
        emptyMessageDiv.style.display = 'none';
    }

    if (!crewCardsContainer) {
        const header = participantsDiv.querySelector('.section-header');
        crewCardsContainer = document.createElement('div');
        crewCardsContainer.className = 'added-crew-card d-flex flex-wrap gap-3';
        header.after(crewCardsContainer);
    } else {
        crewCardsContainer.style.display = 'flex';
    }

    if (clearBtnContainer) {
        clearBtnContainer.style.display = crew.length > 0 ? 'block' : 'none';
    }

    crewCardsContainer.innerHTML = crew.map(member => `
        <div class="added-crew-card p-3 flex-fill text-center position-relative">
            <button class="btn btn-sm btn-danger position-absolute top-0 end-0 m-2"
                    onclick="removeFromCrew('${member.astronautId}', '${missionId}')"
                    style="padding: 2px 6px; font-size: 12px; z-index: 10;">
                <i class="bi bi-x-lg"></i>
            </button>
            <h5 class="fw-bold" style="color:#ffd500;">
                ${member.firstName.toUpperCase()} ${member.lastName.toUpperCase()}
            </h5>
            <p>
                <span style="color: #00d4ff;">▸</span>
                Specialization:
                <span style="color: white;">${member.specialization.toLowerCase()}</span>
            </p>
            <span class="score">Overall score: <span>${member.overallScore}</span></span>
        </div>
    `).join('');
}

function getMissionCrew(missionId) {
    let userMissions = getUserMissionData();
    return userMissions[`mission_${missionId}_crew`] || [];
}

function clearCurrentMissionCrew() {
    const missionId = document.querySelector('.btn-add-participants')?.getAttribute('data-mission-id');
    if (!missionId) {
        showAlert("Mission ID not found!", "danger");
        return;
    }

    clearMissionCrew(missionId);

    document.querySelectorAll(".btn-add-participants").forEach(btn => {
        btn.textContent = "ADD TO CREW";
        btn.disabled = false;
        btn.classList.remove('btn-success');
    });
}

function clearMissionCrew(missionId) {
    let userMissions = getUserMissionData();
    delete userMissions[`mission_${missionId}_crew`];
    saveUserMissionData(userMissions);
    displayCurrentCrew();
    displayCrewMembers();
    const crewCardsContainer = document.querySelector('.added-crew-card');
    if (crewCardsContainer) {
        crewCardsContainer.remove();
    }
    const emptyMessageDiv = document.querySelector('.participants .lead')?.parentElement;
    if (emptyMessageDiv) {
        emptyMessageDiv.style.display = 'block';
    }
    const clearBtnContainer = document.getElementById('clear-crew-btn-container');
    if (clearBtnContainer) {
        clearBtnContainer.style.display = 'none';
    }
    document.getElementById('btn-clear')?.style.setProperty('display', 'none', 'important');
}

function removeFromCrew(astronautId, missionId) {
    let userMissions = getUserMissionData();
    let currentMissionCrew = userMissions[`mission_${missionId}_crew`] || [];

    currentMissionCrew = currentMissionCrew.filter(member => member.astronautId !== astronautId);
    userMissions[`mission_${missionId}_crew`] = currentMissionCrew;
    saveUserMissionData(userMissions);

    displayCurrentCrew();
    displayCrewMembers();
    showAlert("Astronaut removed from crew", "info");

    document.querySelectorAll(".btn-add-participants").forEach(btn => {
        if (btn.getAttribute("data-astronaut-id") === astronautId &&
            btn.getAttribute("data-mission-id") === missionId) {
            btn.textContent = "ADD TO CREW";
            btn.disabled = false;
            btn.classList.remove('btn-success');
        }
    });
    document.getElementById('btn-clear')?.style.setProperty('display', 'none', 'important');
}

function showRisksPopup(button) {
    const missionId = button.getAttribute('data-mission-id');
    const crewSize = parseInt(button.getAttribute('data-crew-size'));

    if (!missionId) return;

    const crew = getMissionCrew(missionId);
    const requiredSpecializations = getRequiredSpecializations();

    const risks = analyzeCrewRisks(crew, crewSize, requiredSpecializations);
    displayRisksPopup(risks, crew, crewSize);
}

function getRequiredSpecializations() {
    const specsText = document.querySelector('p[style*="font-size: 20px"] strong span')?.textContent || '';
    const specs = {};

    if (specsText) {
        specsText.split(',').forEach(item => {
            const match = item.trim().match(/(.+?)\s*-\s*(\d+)/);
            if (match) {
                const specName = match[1].trim().toLowerCase();
                const quantity = parseInt(match[2]);
                specs[specName] = quantity;
            }
        });
    }

    return specs;
}

function analyzeCrewRisks(crew, requiredCrewSize, requiredSpecializations) {
    const risks = {
        crewSizeMatch: crew.length === requiredCrewSize,
        crewCount: crew.length,
        requiredCount: requiredCrewSize,
        specializations: {},
        requiredSpecializations: requiredSpecializations,
        specializationsMismatch: [],
        flightReadiness: [],
        hasErrors: false
    };

    crew.forEach(member => {
        const spec = member.specialization.toLowerCase();
        risks.specializations[spec] = (risks.specializations[spec] || 0) + 1;
    });

    Object.entries(requiredSpecializations).forEach(([spec, required]) => {
        const current = risks.specializations[spec] || 0;
        if (current !== required) {
            risks.specializationsMismatch.push({
                specialization: spec,
                required: required,
                current: current,
                status: current < required ? 'missing' : 'excess'
            });
        }
    });

    if (!risks.crewSizeMatch ||
        risks.specializationsMismatch.length > 0 ||
        risks.flightReadiness.length > 0) {
        risks.hasErrors = true;
    }

    return risks;
}

function displayRisksPopup(risks, crew, requiredCrewSize) {
    const popup = document.getElementById("popup-risks");

    if (!popup) {
        console.error("Popup element not found!");
        return;
    }
    const contentContainer = document.getElementById("risk-analysis-content");

    let content = `
        <div class="risk-analysis">
               <div class="space-background-result">
                <div class="stars"></div>
                <div class="stars2"></div>
                <div class="stars3"></div>
                <div class="success-particles"></div>
            </div>

<div class="risk-analysis-content d-flex justify-content-center flex-row gap-3 mt-0" >
    <!-- CREW STATUS -->
    <div class="crew-status d-flex justify-content-start flex-column" style="flex: 1; max-width: 400px;">
        <h6 style="color: rgb(41,255,248); margin-bottom: 5px; font-weight: 600;">CREW STATUS</h6>
        <div style="background: rgba(48,57,146,0.29); padding: 10px; border-radius: 8px; margin-bottom: 10px; height: 100%;">
            <p style="margin: 8px 0;">
                <span style="color: #ffd500;">▸</span>
                Required crew size: <strong>${requiredCrewSize}</strong>
            </p>
            <p style="margin: 8px 0;">
                <span style="color: #ffd500;">▸</span>
                Current crew size: <strong style="color: ${risks.crewSizeMatch ? '#00ff88' : '#ffffff'}">${risks.crewCount}</strong>
            </p>
            ${!risks.crewSizeMatch ?
        `<p style="color: #e4b600; font-weight: bold; margin-top: 15px; padding: 10px;">
                    ⚠️ Crew size mismatch! ${risks.crewCount < requiredCrewSize ?
            `Need ${requiredCrewSize - risks.crewCount} more astronaut(s)` :
            `${risks.crewCount - requiredCrewSize} astronaut(s) over limit`}<br>
               The mission’s success chance will be reduced! </p>` :
        `<p style="color: #00ff88; margin-top: 15px; padding: 10px; background: rgba(0,255,136,0.1); border-radius: 5px; font-weight: 600;">✓ Crew size is correct!</p>`
    }
        </div>
    </div>

    <!-- SPECIALIZATIONS -->
    <div class="specializations-status d-flex justify-content-start flex-column" style="flex: 1; max-width: 400px;">
        <h6 style="color: #29fff8; margin-bottom: 10px; font-weight: 600;">SPECIALIZATIONS</h6>
        <div style="background: rgba(48,57,146,0.29); padding: 10px; border-radius: 8px; margin-bottom: 10px; height: 100%;">
            ${Object.keys(risks.requiredSpecializations).length > 0 ?
        Object.entries(risks.requiredSpecializations).map(([spec, required]) => {
            const current = risks.specializations[spec] || 0;
            const isCorrect = current === required;
            return `
                        <p style="margin: 8px 0; color: ${isCorrect ? '#00ff88' : '#ffffff'};">
                            <span style="color: #ffd500;">▸</span>
                            ${spec.charAt(0).toUpperCase() + spec.slice(1)}:
                            <strong>${current}/${required}</strong>
                            ${isCorrect ? ' ✓' : ` ${current < required ? '(need ' + (required - current) + ' more)' : '(excess: ' + (current - required) + ')'}`}
                        </p>
                    `;
        }).join('') :
        '<p style="color: #e4b600;">⚠️ No required specializations defined! Mission success reduced!</p>'
    }

            ${risks.specializationsMismatch.length > 0 ?
        `<p style="color: #ffd500; margin-top: 5px; font-weight: bold; padding: 5px;">
                   ⚠️ Missing skills! Success chance reduced!
                </p>` :
        Object.keys(risks.requiredSpecializations).length > 0 ?
            `<p style="color: #00ff88; margin-top: 5px; font-weight: bold; padding: 5px;">
                        ✓ All specializations matched!
                    </p>` : ''
    }
        </div>
    </div>
</div>

<!-- CURRENT CREW MEMBERS -->
<div class="current-crew-popup" style="max-height: 250px; overflow-y: auto;">
    ${crew.length > 0 ?
        (() => {
            const notReadyMembers = crew.filter(m => m.healthStatus !== 'Flight Ready');
            return notReadyMembers.length > 0
                ? `
                <div style="margin-top: 5px; padding: 15px; border: 2px solid rgba(228,182,0,0.3);
                              border-radius: 8px; background: rgba(67,106,253,0.09);">
                    <p style="color: #ffd500;margin-bottom: 10px; font-weight: 600;">⚠️ Attention! If any crew member is not marked as "Ready for Flight", the mission success rate decreases by 10%.</p>
                    ${notReadyMembers.map(m => `
                        <p style="color: #ffffff; font-weight: bold; margin: 8px 0; padding-left: 10px;">
                            • ${m.firstName} ${m.lastName} has status: ${m.healthStatus} (-10% success rate)
                        </p>
                    `).join('')}
                </div>`
                : '';
        })()
        :
        '<p style="color: #e4c904; text-align: center; padding: 20px;">No crew members added yet!</p>'
    }
</div>

        <!-- FOOTER -->
        <div class="popup-footer" style="margin-top: 5px; margin-bottom: 0; text-align: center; padding-top: 0;padding-bottom: 5px;">
            ${!risks.hasErrors && crew.length > 0 ?
        `<p style="color: #00ff88; font-size: 20px; font-weight: bold;">
                    ✓ Mission is ready for launch!
                </p>` :
        `<p style="color: #e4c904; font-size: 20px; font-weight: bold;">
                   Mission has critical issues that need to be resolved!
                </p>`
    }
        </div>
    `;

    contentContainer.innerHTML = content;
    popup.style.display = "flex";

    const closeBtn = document.getElementById("popup-risks-close");
    if (closeBtn) {
        closeBtn.onclick = () => popup.style.display = "none";
    }

    popup.onclick = (e) => {
        if (e.target.id === "popup-risks") {
            popup.style.display = "none";
        }
    };

    document.onkeydown = (e) => {
        if (e.key === "Escape") {
            popup.style.display = "none";
        }
    };
}

function showMissionPopup(button) {
    const missionId = button.getAttribute('data-mission-id');
    const crewSize = button.getAttribute('data-crew-size');
    if (!missionId) return;
    const crew = getMissionCrew(missionId);
    const requiredSpecializations = getRequiredSpecializations();
    displayResultsPopup(crew, crewSize, requiredSpecializations);
}

function displayResultsPopup(missionReport, crew, crewSize) {
    console.log("displayResultsPopup called");
    console.log("missionReport:", missionReport);
    console.log("crew:", crew);
    console.log("crewSize:", crewSize);

    const popup = document.getElementById("popup-start-mission");
    const contentContainer = document.getElementById("mission-results-content");

    if (!popup) {
        console.error("Popup element not found!");
        return;
    }
    if (!missionReport) {
        console.error("missionReport is null or undefined!");
        return;
    }

    const isSuccess = missionReport.isSuccessful ?? missionReport.successful;
    console.log("✅ isSuccess value:", isSuccess);
    console.log("✅ isSuccess type:", typeof isSuccess);

    if (isSuccess === undefined || isSuccess === null) {
        console.error("❌ isSuccessful field is missing in missionReport!");
        console.error("Available fields:", Object.keys(missionReport));
        return;
    }

    popup.style.display = "flex";

    contentContainer.innerHTML = `
        <div class="mission-loading-screen">
            <div class="space-background">
                <div class="stars"></div>
                <div class="stars2"></div>
                <div class="stars3"></div>
            </div>

            <div class="loading-content">
                <div class="rocket-container">
                    <div class="rocket">🚀</div>
                    <div class="rocket-flame"></div>
                </div>

                <div class="loading-status">
                    <div class="status-line" id="status-1">
                        <span class="status-icon">⚡</span>
                        <span class="status-text">INITIALIZING MISSION PROTOCOL</span>
                        <span class="dots"></span>
                    </div>
                    <div class="status-line" id="status-2" style="opacity: 0;">
                        <span class="status-icon">👨‍🚀</span>
                        <span class="status-text">ASSEMBLING CREW MANIFEST</span>
                        <span class="dots"></span>
                    </div>
                    <div class="status-line" id="status-3" style="opacity: 0;">
                        <span class="status-icon">📊</span>
                        <span class="status-text">CALCULATING RISK PARAMETERS</span>
                        <span class="dots"></span>
                    </div>
                    <div class="status-line" id="status-4" style="opacity: 0;">
                        <span class="status-icon">🛸</span>
                        <span class="status-text">ANALYZING TRAJECTORY</span>
                        <span class="dots"></span>
                    </div>
                    <div class="status-line" id="status-5" style="opacity: 0;">
                        <span class="status-icon">✓</span>
                        <span class="status-text">FINALIZING MISSION DATA</span>
                        <span class="dots"></span>
                    </div>
                </div>

                <div class="progress-bar-container">
                    <div class="progress-bar" id="mission-progress"></div>
                    <div class="progress-glow"></div>
                </div>
            </div>
        </div>
    `;
    const statusLines = [
        document.getElementById('status-1'),
        document.getElementById('status-2'),
        document.getElementById('status-3'),
        document.getElementById('status-4'),
        document.getElementById('status-5')
    ];
    const progressBar = document.getElementById('mission-progress');
    let currentStatus = 0;

    const loadingSequence = setInterval(() => {
        if (currentStatus < statusLines.length) {
            statusLines[currentStatus].style.opacity = '1';
            statusLines[currentStatus].style.animation = 'slideInLeft 0.5s ease-out';

            progressBar.style.width = `${((currentStatus + 1) / statusLines.length) * 100}%`;

            if (currentStatus > 0) {
                statusLines[currentStatus - 1].classList.add('completed');
            }

            currentStatus++;
        } else {
            clearInterval(loadingSequence);

            setTimeout(() => {
                displayFinalResults(contentContainer, isSuccess, missionReport, crew, crewSize);
            }, 800);
        }
    }, 600);

    const closeBtn = document.getElementById("popup-mission-close");
    if (closeBtn) {
        closeBtn.onclick = () => {
            popup.style.display = "none";
            window.location.reload();
        };
    }

    popup.onclick = (e) => {
        if (e.target.id === "popup-start-mission") {
            popup.style.display = "none";
            window.location.reload();
        }
    };

    document.onkeydown = (e) => {
        if (e.key === "Escape") {
            popup.style.display = "none";
            window.location.reload();
        }
    };
}

function displayFinalResults(contentContainer, isSuccess, missionReport, crew, crewSize) {
    let content = '';

    if (isSuccess) {
        console.log("🎉 Displaying SUCCESS popup");

        const payment = missionReport.paymentAmount?.toLocaleString() || "0";
        const salary = missionReport.totalSalary?.toLocaleString() || "0";

        content = `
        <div class="mission-results-success">
            <div class="space-background-result">
                <div class="stars"></div>
                <div class="stars2"></div>
                <div class="stars3"></div>
                <div class="success-particles"></div>
            </div>

            <div class="result-header success-header">
                <div class="status-badge success-badge">
                    <span class="badge-icon">✓</span>
                </div>
                <h3 class="result-title success-title">MISSION ACCOMPLISHED!</h3>
                <div class="success-celebration">
                    <span class="celebration-emoji">⭐</span>
                    <span class="celebration-emoji">⭐</span>
                    <span class="celebration-emoji">⭐</span>
                    <span class="celebration-emoji">⭐</span>
                    <span class="celebration-emoji">⭐</span>
                </div>
                <p class="result-subtitle">Outstanding performance! All objectives achieved.</p>
            </div>

            <div class="mission-stats-container">
                <div class="stat-card primary-stats">
                    <h4 class="stat-header">
                        <span class="header-icon">📊</span>
                        MISSION OVERVIEW
                    </h4>
                    <div class="stat-grid">
                        <div class="stat-item">
                            <span class="stat-label">Mission</span>
                            <span class="stat-value">${missionReport.missionName}</span>
                        </div>
                        <div class="stat-item">
                            <span class="stat-label">Destination</span>
                            <span class="stat-value">${missionReport.destinationName}</span>
                        </div>
                        <div class="stat-item highlight-stat">
                            <span class="stat-label">Success Rate</span>
                            <span class="stat-value success-rate">${missionReport.successChance}%</span>
                        </div>
                        <div class="stat-item">
                            <span class="stat-label">Crew Size</span>
                            <span class="stat-value">${crew.length}/${crewSize}</span>
                        </div>
                        <div class="stat-item highlight-stat payment-stat">
                            <span class="stat-label">Mission Revenue</span>
                            <span class="stat-value payment-value">${payment} $</span>
                        </div>
                        <div class="stat-item highlight-stat salary-stat">
                            <span class="stat-label">Total mission expenses:</span>
                            <span class="stat-value salary-value">${salary} $</span>
                        </div>
           
                    </div>
                </div>

                <div class="stat-card crew-stats">
                    <h4 class="stat-header">
                        <span class="header-icon">👨‍🚀</span>
                        CREW ROSTER
                    </h4>
                    <div class="crew-list">
                        ${crew.map((member, index) => `
                            <div class="crew-member" style="animation-delay: ${index * 0.1}s">
                                <div class="crew-avatar">${member.firstName.charAt(0)}${member.lastName.charAt(0)}</div>
                                <div class="crew-info">
                                    <p class="crew-name">${member.firstName.toUpperCase()} ${member.lastName.toUpperCase()}</p>
                                    <p class="crew-role">${member.specialization}</p>
                                </div>
                                <div class="crew-status-icon">✓</div>
                            </div>
                        `).join('')}
                    </div>
                </div>

                <div class="d-flex justify-content-center m-2">
                <button class="btn-outline-info" type="button">
                DOWNLOAD PDF
               </button>
                   </div>
            </div>
        </div>`;

    } else {
        console.log("❌ Displaying FAILURE popup");
        const payment = missionReport.paymentAmount?.toLocaleString() || "0";
        const salary = missionReport.totalSalary?.toLocaleString() || "0";
        content = `
        <div class="mission-results-failure">
            <div class="space-background-result failure-bg">
                <div class="stars"></div>
                <div class="stars2"></div>
                <div class="stars3"></div>
                <div class="failure-particles"></div>
            </div>

            <div class="result-header failure-header">
                <div class="status-badge failure-badge">
                    <span class="badge-icon">✗</span>
                </div>
                <h3 class="result-title failure-title">MISSION FAILED !</h3>
             <p class="result-subtitle">Critical system failure detected</p>
              <span class="stat-label">Total crew salary you've paid:</span>
                            <span class="stat-value salary-value">${salary} $</span>
            </div>
                           
            <div class="mission-stats-container">

                       ${missionReport.alienAttack ? `
                    <div class="alien-attack-alert">
                        <div class="alert-pulse"></div>
                        <div class="alien-icon">👽</div>
                        <h4 class="alert-title">ALIEN ENCOUNTER DETECTED</h4>
                        <p class="alert-description">
                            The mission was compromised by an unexpected extraterrestrial threat!
                        </p>
                    </div>
                ` : ''}

                <div class="stat-card failure-analysis">

                    <h4 class="stat-header">
                        <span class="header-icon">🔍</span>
                        FAILURE ANALYSIS
                    </h4>

                    ${missionReport.issues && missionReport.issues.length > 0 ? `
                        <div class="issues-list">
                            <p class="issues-header">Critical Issues Detected:</p>
                            ${missionReport.issues.map((issue, index) => `
                                <div class="issue-item" style="animation-delay: ${index * 0.1}s">
                                    <span class="issue-icon">⚠️</span>
                                    <span class="issue-text">${issue}</span>
                                </div>
                            `).join('')}
                        </div>
                    ` : `
                        <p class="generic-failure">Mission failed due to unforeseen circumstances.</p>
                    `}

                    <div class="failure-stats">
                        <div class="stat-item">
                            <span class="stat-label">Success Probability</span>
                            <span class="stat-value failure-rate">${missionReport.successChance}%</span>
                        </div>
                        <div class="stat-item">
                            <span class="stat-label">Crew Size</span>
                            <span class="stat-value">${crew.length}/${crewSize}</span>
                        </div>
                    </div>

                </div>
                  <div class="d-flex justify-content-center m-2">
                <button class="btn-outline-info" type="button">
                DOWNLOAD PDF
               </button>
                   </div>
            </div>
        </div>`;
    }

    contentContainer.innerHTML = content;
    contentContainer.classList.add('fade-in-result');

    // Add PDF download handler
    const downloadBtn = contentContainer.querySelector('button');
    if (downloadBtn) {
        downloadBtn.addEventListener('click', () => {
            downloadMissionReportPdf(isSuccess, missionReport, crew, crewSize);
        });
    }
}

async function downloadMissionReportPdf(isSuccess, missionReport, crew, crewSize) {
    const reportData = {
        isSuccessful: isSuccess,
        missionName: missionReport.missionName,
        destinationName: missionReport.destinationName,
        successChance: missionReport.successChance,
        paymentAmount: missionReport.paymentAmount,
        totalSalary: missionReport.totalSalary,
        alienAttack: missionReport.alienAttack,
        issues: missionReport.issues || [],
        participants: crew.map(member => ({
            astronautName: `${member.firstName} ${member.lastName}`,
            specialization: member.specialization
        })),
        crewSize: crewSize
    };

    const accessToken = window.MISSION_CONFIG?.accessToken || sessionStorage.getItem('accessToken');

    if (!accessToken) {
        console.error("No token found!");
        showAlert("Authentication token missing. Please log in again.", "danger");
        return;
    }

    try {
        const response = await fetch('http://localhost:8080/api/mission-reports/export/pdf', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${accessToken}`
            },
            body: JSON.stringify(reportData)
        });


        if (!response.ok) throw new Error('Failed to generate PDF');

        const blob = await response.blob();
        const url = window.URL.createObjectURL(blob);
        const a = document.createElement('a');
        a.href = url;
        a.download = `mission-report-${Date.now()}.pdf`;
        document.body.appendChild(a);
        a.click();
        window.URL.revokeObjectURL(url);
        document.body.removeChild(a);
    } catch (error) {
        console.error('Error downloading PDF:', error);
        alert('Failed to download PDF. Please try again.');
    }

}

// START MISSION BUTTON HANDLER
document.querySelector('.btn-start-mission').addEventListener('click', async () => {
    const button = event.currentTarget;
    if (button.disabled) return;

    const missionId = document.querySelector('.btn-add-participants')?.getAttribute('data-mission-id');
    if (!missionId) {
        showAlert("Mission ID not found!", "danger");
        return;
    }

    function normalizeHealthStatus(status) {
        if (!status) return 'FLIGHT_READY';
        return status.trim().toUpperCase().replace(/\s+/g, '_');
    }

    const crew = getMissionCrew(missionId);
    if (crew.length === 0) {
        showAlert("No crew selected!", "warning");
        return;
    }

    const mission = {
        id: missionId,
        participants: crew.map(member => ({
            missionId: parseInt(missionId),
            astronautId: parseInt(member.astronautId ?? member.id),
            astronautName: `${member.firstName} ${member.lastName}`,
            healthStatus: normalizeHealthStatus(member.healthStatus),
            specialization: member.specialization?.toUpperCase(),
            overallScore: member.overallScore ?? 0
        }))
    };

    const accessToken = window.MISSION_CONFIG?.accessToken || sessionStorage.getItem('accessToken');
    const userId = window.MISSION_CONFIG?.userId || sessionStorage.getItem('currentUserId');

    if (!accessToken) {
        console.error("No token found!");
        showAlert("Authentication token missing. Please log in again.", "danger");
        return;
    }

    button.disabled = true;
    button.textContent = "🚀 LAUNCHING...";

    try {
        const response = await fetch(`http://localhost:8080/api/missions/${missionId}/start`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${accessToken}`
            },
            body: JSON.stringify(mission)
        });

        if (!response.ok) {
            const errorText = await response.text();
            console.error("Error response:", errorText);
            throw new Error(`Error starting mission: ${response.status} - ${errorText}`);
        }

        const result = await response.json();
        const missionReport = result.missionReport;
        const updatedBudget = result.updatedBudget;
        const crewSize = parseInt(document.querySelector('.btn-add-participants')?.getAttribute('data-crew-size')) || crew.length;

        if (missionReport.totalSalary && updatedBudget.currentBudget < 6000) {
            showAlert("Unfortunately, your budget is too low for any mission! ", "danger");
            button.disabled = false;
            button.textContent = "START MISSION";
            return;
        } else if (missionReport.totalSalary && updatedBudget.currentBudget < missionReport.totalSalary) {
            showAlert("⚠️ Budget too low to pay this crew.\nTry reducing the crew size or attempting an easier mission.", "warning");
            button.disabled = false;
            button.textContent = "START MISSION";
            return;
        }

        displayResultsPopup(missionReport, crew, crewSize);

        updateBudgetUI(updatedBudget.currentBudget, missionReport.totalSalary);

        const startedMissionsKey = 'startedMissions';
        let startedMissions = JSON.parse(sessionStorage.getItem(startedMissionsKey) || '[]');
        if (!startedMissions.includes(missionId)) {
            startedMissions.push(missionId);
            sessionStorage.setItem(startedMissionsKey, JSON.stringify(startedMissions));
        }

        let userMissions = getUserMissionData();
        delete userMissions[`mission_${missionId}_crew`];
        saveUserMissionData(userMissions);

        displayCurrentCrew();
        displayCrewMembers();

        document.querySelectorAll(".btn-add-participants").forEach(btn => {
            if (btn.getAttribute("data-mission-id") === missionId) {
                btn.textContent = "ADD TO CREW";
                btn.disabled = false;
                btn.classList.remove('btn-success');
            }
        });

    } catch (error) {
        console.error("Failed to start mission:", error);
        showAlert(`Failed to start mission: ${error.message}`, "danger");
        button.disabled = false;
        button.textContent = "START MISSION";
    }
});


document.addEventListener("DOMContentLoaded", () => {
    const budgetElement = document.getElementById("currentBudget");
    const progressBar = document.getElementById("budgetProgress");
    const notification = document.getElementById("budgetChangeNotification");

    if (!budgetElement) return;

    let previousBudget = parseInt(budgetElement.textContent.replace(/,/g, '')) || 0;
    updateBudgetColor(previousBudget);


    function updateBudgetColor(budget) {
        if (budget > 1000000) {
            budgetElement.style.color = "#38aa8c";
            progressBar.style.backgroundColor = "#38a97e";
        } else if (budget >= 500000) {
            budgetElement.style.color = "#ffd500";
            progressBar.style.backgroundColor = "#ffd500";
        } else if (budget >= 100000) {
            budgetElement.style.color = "#ff9500";
            progressBar.style.backgroundColor = "#ff9500";
        } else {
            budgetElement.style.color = "#ff0000";
            progressBar.style.backgroundColor = "#ff0000";
        }
    }

    window.updateBudgetUI = function (newBudget, totalSalary = null) {
        const change = newBudget - previousBudget;

        budgetElement.textContent = newBudget.toLocaleString();

        updateBudgetColor(newBudget);

        if (change !== 0) {
            setTimeout(() => {
                notification.textContent = (change > 0 ? "+ " : "− ") + Math.abs(change).toLocaleString() + " $";
                notification.style.color = change > 0 ? "#38a86b" : "#ff0000";
                notification.classList.add("show");

                setTimeout(() => {
                    notification.classList.remove("show");
                }, 50000);
            }, 5000);
        }

        if (totalSalary && newBudget < totalSalary) {
            showAlert("⚠️ Budget too low to pay this crew.\nTry reducing the crew size or attempting an easier mission.", "danger");
        }

        previousBudget = newBudget;
    };
});