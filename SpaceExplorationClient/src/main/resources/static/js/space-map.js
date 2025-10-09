// Global variables
let scene, camera, renderer;
let spaceEntities = [];
let destinationsData = [];
let selectedSpaceEntity = null;
let autoRotate = true;
let textureLoader = new THREE.TextureLoader();


// Initialize the clean 3D space map (no WebGL errors)
function initSpaceMap() {
    console.log(' Initializing Clean Space Map...');

    // Get destinations data from Thymeleaf
    if (typeof DESTINATIONS_DATA !== 'undefined' && DESTINATIONS_DATA && DESTINATIONS_DATA.length > 0) {
        destinationsData = DESTINATIONS_DATA;
        console.log('Database data loaded:', destinationsData.length, 'destinations');
    } else {
        console.error('No database data available');
        return;
    }

    try {
        scene = new THREE.Scene();
        // Create camera
        camera = new THREE.PerspectiveCamera(75, window.innerWidth / window.innerHeight, 0.1, 1000);
        camera.position.set(0, 0, 15);

        // Create renderer with basic settings
        setupLighting();
        createStars();
        createSpaceEntities();
        setupControls();
        setupEventListeners();
        setTimeout(() => {
            animate();
            updateStats();
        }, 500);

    } catch (error) {
        console.error('Initialization failed:', error);
    }
}

function setupLighting() {
    // Strong ambient light
    const ambientLight = new THREE.AmbientLight(0x404040, 1.5);
    scene.add(ambientLight);

    // Directional light
    const directionalLight = new THREE.DirectionalLight(0xffffff, 1);
    directionalLight.position.set(10, 10, 5);
    scene.add(directionalLight);

    console.log('Lighting added');
}

function createStars() {
    const canvas = document.getElementById('three-canvas');
    if (!canvas) {
        throw new Error('Canvas not found!');
    }
    renderer = new THREE.WebGLRenderer({
        canvas: canvas,
        antialias: true,
        alpha: false
    });
    renderer.setSize(window.innerWidth, window.innerHeight);
    renderer.setPixelRatio(Math.min(window.devicePixelRatio, 2));

    const starGeometry = new THREE.BufferGeometry();
    const starCount = 2000;
    const starPositions = new Float32Array(starCount * 3);
    const sizes = new Float32Array(starCount);
    for (let i = 0; i < starCount; i += 1) {
        const i3 = i * 3;
        starPositions[i3] = (Math.random() - 0.5) * 400;
        starPositions[i3 + 1] = (Math.random() - 0.5) * 400;
        starPositions[i3 + 2] = (Math.random() - 0.5) * 400;
        sizes[i] = Math.random() * 2 + 0.5;
    }
    starGeometry.setAttribute('position', new THREE.BufferAttribute(starPositions, 3));
    starGeometry.setAttribute('size', new THREE.Float32BufferAttribute(sizes, 1));

    const textureCanvas = document.createElement('canvas');
    textureCanvas.width = 64;
    textureCanvas.height = 64;
    const ctx = textureCanvas.getContext('2d');

    const gradient = ctx.createRadialGradient(32, 32, 0, 32, 32, 32);
    gradient.addColorStop(0, 'rgba(255, 255, 255, 1)');
    gradient.addColorStop(0.2, 'rgba(255, 255, 255, 0.8)');
    gradient.addColorStop(0.5, 'rgba(255, 255, 255, 0.3)');
    gradient.addColorStop(1, 'rgba(255, 255, 255, 0)');
    ctx.fillStyle = gradient;
    ctx.fillRect(0, 0, 64, 64);

    const starTexture = new THREE.CanvasTexture(textureCanvas);

    const starMaterial = new THREE.PointsMaterial({
        size: 2,
        sizeAttenuation: true,
        map: starTexture,
        transparent: true,
        opacity: 0.9,
        vertexColors: false,
        blending: THREE.AdditiveBlending,
        depthWrite: false
    });

    const stars = new THREE.Points(starGeometry, starMaterial);
    scene.add(stars);
}

function createSpaceEntities() {
    console.log('Creating simple space entities...');

    spaceEntities.forEach(planet => scene.remove(planet));
    spaceEntities = [];

    destinationsData.forEach((destination, index) => {
        try {
            const spaceEntity = createEntity(destination, index);
            if (spaceEntity) {
                scene.add(spaceEntity);
                spaceEntities.push(spaceEntity);
                console.log(`Created planet: ${destination.destinationName}`);
            }
        } catch (error) {
            console.error(`Failed to create planet ${destination.destinationName}:`, error);
        }
    });

    console.log(`Created ${spaceEntities.length} space entities.`);
    updateStats();
}

function createEntity(destination, index) {
    const entityName = destination.destinationName.toLowerCase();
    const radius = 1.5;
    const geometry = new THREE.SphereGeometry(radius, 32, 32);

    // SIMPLE MATERIALS - NO TEXTURES TO AVOID WEBGL ERRORS
    let texturePath;
    switch (entityName) {
        case 'mars':
            texturePath = '/images/mars.jpg';
            break;
        case 'earth':
            texturePath = '/images/earth2.jpg';
            break;
        case 'aurelia':
            texturePath = '/images/aurelia.jpg';
            break;
        case 'proxima centauri':
            texturePath = '/images/proxima.jpg';
            break;
        case 'eros':
            texturePath = '/images/asteroid.jpg';
            break;
        default:
            texturePath = '/images/default.jpg';
    }

    const material = new THREE.MeshLambertMaterial({
        map: textureLoader.load(texturePath)
    });

    const entity = new THREE.Mesh(geometry, material);

    // Position spaceEntities clearly visible
    const angle = (index / destinationsData.length) * Math.PI * 2;
    const distance = 8;

    entity.position.x = Math.cos(angle) * distance;
    entity.position.z = Math.sin(angle) * distance;
    entity.position.y = (index % 2 === 0 ? 1 : -1) * (0.5 + Math.random()) + 1;

    if (entityName === 'proxima centauri') {
        const spriteMaterial = new THREE.SpriteMaterial({
            map: new THREE.TextureLoader().load('/images/glow.png'),
            color: 0xffaa00,
            transparent: true,
            blending: THREE.AdditiveBlending
        });

        const sprite = new THREE.Sprite(spriteMaterial);
        sprite.scale.set(radius * 5, radius * 5, 1);
        entity.add(sprite);

        entity.userData.glow = sprite;
    }

    // Store database data
    entity.userData = destination;
    entity.name = destination.destinationName;
    entity.visible = true;

    console.log(`Created clickable space entity: ${destination.destinationName} at position:`, entity.position);
    return entity;
}

function setupControls() {
    let isMouseDown = false;
    let mouseX = 0, mouseY = 0;
    let targetRotationY = 0, targetRotationX = 0;

    const canvas = renderer.domElement;

    canvas.addEventListener('mousedown', (event) => {
        isMouseDown = true;
        mouseX = event.clientX;
        mouseY = event.clientY;
    });

    canvas.addEventListener('mouseup', () => {
        isMouseDown = false;
    });

    canvas.addEventListener('mousemove', (event) => {
        if (!isMouseDown) return;

        const deltaX = event.clientX - mouseX;
        const deltaY = event.clientY - mouseY;

        targetRotationY += deltaX * 0.005;
        targetRotationX += deltaY * 0.005;
        targetRotationX = Math.max(-Math.PI / 3, Math.min(Math.PI / 3, targetRotationX));

        mouseX = event.clientX;
        mouseY = event.clientY;
    });

    canvas.addEventListener('wheel', (event) => {
        event.preventDefault();
        const zoomSpeed = 0.1;
        const zoomFactor = event.deltaY > 0 ? 1 + zoomSpeed : 1 - zoomSpeed;

        camera.position.multiplyScalar(zoomFactor);
        camera.position.clampLength(8, 50);
    });

    // Smooth camera updates
    function updateCameraControls() {
        const radius = camera.position.length();
        camera.position.x = radius * Math.sin(targetRotationY) * Math.cos(targetRotationX);
        camera.position.y = radius * Math.sin(targetRotationX);
        camera.position.z = radius * Math.cos(targetRotationY) * Math.cos(targetRotationX);
        camera.lookAt(0, 0, 0);
    }

    window.updateCameraControls = updateCameraControls;
    console.log('Controls setup');
}

function setupEventListeners() {
    console.log('Setting up event listeners...');

    // Reset view button
    const resetBtn = document.getElementById('reset-view');
    if (resetBtn) {
        resetBtn.addEventListener('click', () => {
            camera.position.set(0, 0, 15);
            camera.lookAt(0, 0, 0);
        });
    }

    // Auto rotate toggle
    const autoRotateToggle = document.getElementById('auto-rotate');
    if (autoRotateToggle) {
        autoRotateToggle.addEventListener('change', (event) => {
            autoRotate = event.target.checked;
        });
    }

    // FIXED CLICK DETECTION
    const canvas = document.getElementById('three-canvas');
    if (canvas) {
        canvas.addEventListener('click', onPlanetClick, false);
        console.log('Click listener added to canvas');
    }

    window.addEventListener('resize', onWindowResize);
    console.log('All event listeners ready');
}

function onPlanetClick(event) {
    console.log('CLICK DETECTED!', event.clientX, event.clientY);

    const raycaster = new THREE.Raycaster();
    const mouse = new THREE.Vector2();

    // Get normalized device coordinates (NDC)
    const canvas = renderer.domElement;
    const rect = canvas.getBoundingClientRect();

    mouse.x = ((event.clientX - rect.left) / rect.width) * 2 - 1;
    mouse.y = -((event.clientY - rect.top) / rect.height) * 2 + 1;

    console.log('Mouse coords:', mouse.x, mouse.y);

    raycaster.setFromCamera(mouse, camera);

    const intersects = raycaster.intersectObjects(spaceEntities, true);

    console.log('Checking', spaceEntities.length, 'spaceEntities');
    console.log('Found', intersects.length, 'intersections', intersects);

    if (intersects.length > 0) {
        let clickedEntity = intersects[0].object;

        if (clickedEntity.parent && clickedEntity.parent.userData && clickedEntity.parent.userData.destinationName) {
            clickedEntity = clickedEntity.parent;
        }
        console.log('ENTITY CLICKED:', clickedEntity.name || clickedEntity.userData?.destinationName);
        selectSpaceEntity(clickedEntity);
    } else {
        console.log('No entity hit');
    }
}

function selectSpaceEntity(entity) {
    selectedSpaceEntity = entity;
    const destination = entity.userData;
    console.log('ENTITY SELECTED:', destination.destinationName);

    updateInfoPanel(destination);
    updateStats();

    if (destination) {
        showPlanetPopup(destination);
    } else {
        console.warn("No destination data found for", entity.name);
    }
}


function showPlanetPopup(destination) {
    document.getElementById("popup-title").textContent = `${destination.destinationName}`;
    document.getElementById("popup-entity-description").textContent = destination.description || "No description";
    document.getElementById("popup-distance").textContent = destination.distanceFromEarth || "Unknown";
    document.getElementById("popup-gravity").textContent = destination.gravity || "No gravity";

    const popup = document.getElementById("planet-popup");
    popup.style.display = "flex";

    const viewMissionsBtn = document.getElementById("btn-view-mission");
    const newBtn = viewMissionsBtn.cloneNode(true);
    viewMissionsBtn.parentNode.replaceChild(newBtn, viewMissionsBtn);

    newBtn.addEventListener('click', () => {
        popup.style.display = "none";
        loadAndShowMissions(destination.id, destination.destinationName);
    });
    document.getElementById("popup-close").onclick = () => {
        popup.style.display = "none";
    };

    popup.onclick = (e) => {
        if (e.target.id === "planet-popup") {
            popup.style.display = "none";
        }
    };

    document.onkeydown = (e) => {
        if (e.key === "Escape") {
            popup.style.display = "none";
        }
    };
}

async function loadAndShowMissions(destinationId, destinationName) {
    try {
        const response = await fetch(`/destinations/missions/${destinationId}`);
        if (!response.ok) throw new Error('Failed to fetch missions');

        const missions = await response.json();
        console.log('Loaded missions:', missions);

        document.getElementById("planet-popup").style.display = "none";
        if (missions.length === 0) {
            alert('No missions available for this destination.');
            return;
        }

        let missionsHTML = '';
        missions.forEach(mission => {
            let specializationsStr = '';
            if (mission.specializations && Array.isArray(mission.specializations)) {
                specializationsStr = mission.specializations
                    .map(spec => {
                        const specName = spec.specialization || '';
                        const qty = spec.quantity || '';
                        return `${specName.toLowerCase()} - ${qty}`;
                    })
                    .join(', ');
            }
                const missionData = {
                    id: mission.id || '',
                    name: (mission.name).toUpperCase(),
                    code: mission.code || '',
                    description: mission.description,
                    destination: mission.destinationName || destinationName,
                    difficulty: mission.difficultyLevel.toLowerCase(),
                    score: mission.scoreValue || '',
                    crew: mission.crewSize || '',
                    issues: mission.potentialIssues || '',
                    duration: mission.durationDays || '',
                    payment: mission.paymentAmount || '',
                    specializations: specializationsStr,
                    image: mission.imgUrl || ''
                };
                missionsHTML += `
    <div class="btn-view-mission mission-destinations p-3 border border-info rounded" style="background: rgba(0,0,0,0.3);"
                             data-id="${missionData.id}"
                             data-name="${missionData.name}"
                             data-code="${missionData.code}"
                             data-description="${missionData.description}"
                             data-destination="${missionData.destination}"
                             data-difficulty="${missionData.difficulty}"
                             data-score="${missionData.score}"
                             data-crew="${missionData.crew}"
                             data-issues="${missionData.issues}"
                             data-duration="${missionData.duration}"
                             data-payment="${missionData.payment}"
                             data-specializations="${missionData.specializations}"
                             data-image="${mission.imgUrl}">
                    </div>
                `;
            });

        const missionsContainer = document.createElement('div');
        missionsContainer.innerHTML = missionsHTML;
        document.body.appendChild(missionsContainer);

        const missionButtons = Array.from(missionsContainer.querySelectorAll('.btn-view-mission'));
        if (missionButtons.length > 0) {
            window.showPopup(missionButtons[0]);
        }

    } catch (error) {
        console.error('Error loading missions:', error);
        alert('Failed to load missions. Please try again.');
    }
}

function updateInfoPanel(destination) {
    const infoPanel = document.getElementById('planet-info');
    if (infoPanel && destination) {
        infoPanel.innerHTML = `
            <h5 class="text-info mb-3 " style="font-family: 'Orbitron', monospace !important;">
                <i class="bi bi-globe me-2"></i>${destination.destinationName}
            </h5>
            <div class="mb-2">
                <strong>ID:</strong> ${destination.id}
            </div>
            <div class="mb-2">
                <strong>Type:</strong> ${destination.entityType || 'Unknown'}
            </div>
        `;
    }
}


function animate() {
    requestAnimationFrame(animate);
    // Update camera controls
    if (window.updateCameraControls) {
        window.updateCameraControls();
    }
    // Auto rotate spaceEntities
    if (autoRotate && spaceEntities.length > 0) {
        spaceEntities.forEach((planet, index) => {
            planet.rotation.y += 0.002;
            planet.rotation.x += 0.001;
        });
    }
    renderer.render(scene, camera);
}

function onWindowResize() {
    camera.aspect = window.innerWidth / window.innerHeight;
    camera.updateProjectionMatrix();
    renderer.setSize(window.innerWidth, window.innerHeight);
}

function updateStats() {
    const destCount = document.getElementById('destination-count');
    const selectedName = document.getElementById('selected-planet');
    if (destCount) destCount.textContent = destinationsData.length;
    if (selectedName) selectedName.textContent = selectedSpaceEntity ? selectedSpaceEntity.name : 'None';
}


// Initialize
document.addEventListener('DOMContentLoaded', () => {
    console.log('DOM ready - Starting CLEAN space map...');
    setTimeout(() => {
        initSpaceMap();
    }, 200);
});