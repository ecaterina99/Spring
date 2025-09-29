// Global variables
let scene, camera, renderer;
let planets = [];
let destinationsData = [];
let selectedPlanet = null;
let autoRotate = true;

// Initialize the clean 3D space map (no WebGL errors)
function initSpaceMap() {
    console.log(' Initializing Clean Space Map...');
    updateLoadingProgress(10, 'Getting database data...');

    // Get destinations data from Thymeleaf
    if (typeof DESTINATIONS_DATA !== 'undefined' && DESTINATIONS_DATA && DESTINATIONS_DATA.length > 0) {
        destinationsData = DESTINATIONS_DATA;
        console.log('Database data loaded:', destinationsData.length, 'destinations');
    } else {
        console.error('No database data available');
        updateLoadingProgress(100, 'No database data available');
        setTimeout(hideLoadingScreen, 2000);
        return;
    }

    try {
        updateLoadingProgress(20, 'Creating scene...');

        // Create simple scene
        scene = new THREE.Scene();
      //  scene.background = new THREE.Color(0x000011); // Simple dark blue background

        updateLoadingProgress(40, 'Setting up camera...');

        // Create camera
        camera = new THREE.PerspectiveCamera(75, window.innerWidth / window.innerHeight, 0.1, 1000);
        camera.position.set(0, 0, 15);

        updateLoadingProgress(60, 'Creating renderer...');

        // Create renderer with basic settings
        const canvas = document.getElementById('three-canvas');
        if (!canvas) {
            throw new Error('Canvas not found!');
        }

        renderer = new THREE.WebGLRenderer({
            canvas: canvas,
            antialias: true,
            alpha: false // Disable alpha to avoid context issues
        });
        renderer.setSize(window.innerWidth, window.innerHeight);
        renderer.setPixelRatio(Math.min(window.devicePixelRatio, 2));

        const starGeometry = new THREE.BufferGeometry();
        const starCount = 2000;
        const starPositions = new Float32Array(starCount * 3);
        for (let i = 0; i < starCount; i += 1) {
            const i3 = i * 3;
            starPositions[i3] = (Math.random() - 0.5) * 400;
            starPositions[i3 + 1] = (Math.random() - 0.5) * 400;
            starPositions[i3 + 2] = (Math.random() - 0.5) * 400;
        }
        starGeometry.setAttribute('position', new THREE.BufferAttribute(starPositions, 3));
        const starMaterial = new THREE.PointsMaterial({
            color: 0x9fb5ff,
            size: 0.55,
            transparent: true,
            opacity: 0.65,
        });
        const stars = new THREE.Points(starGeometry, starMaterial);
        scene.add(stars);


        updateLoadingProgress(70, 'Adding lighting...');
        setupLighting();

        updateLoadingProgress(80, 'Creating planets...');
        createSimplePlanets();

        updateLoadingProgress(90, 'Setting up controls...');
        setupControls();
        setupEventListeners();

        updateLoadingProgress(100, 'Ready!');

        setTimeout(() => {
            hideLoadingScreen();
            animate();
            updateStats();
        }, 500);

    } catch (error) {
        console.error('Initialization failed:', error);
        updateLoadingProgress(100, 'Error: ' + error.message);
        setTimeout(hideLoadingScreen, 2000);
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

function createSimplePlanets() {
    console.log('Creating simple planets (NO TEXTURES)...');

    planets.forEach(planet => scene.remove(planet));
    planets = [];

    destinationsData.forEach((destination, index) => {
        try {
            const planet = createSimplePlanet(destination, index);
            if (planet) {
                scene.add(planet);
                planets.push(planet);
                console.log(`Created planet: ${destination.destinationName}`);
            }
        } catch (error) {
            console.error(`Failed to create planet ${destination.destinationName}:`, error);
        }
    });

    console.log(`Created ${planets.length} planets`);
    updateStats();
}

function createSimplePlanet(destination, index) {
    const planetName = destination.destinationName.toLowerCase();
    const radius = 1.5; // Fixed size for easier clicking
    const geometry = new THREE.SphereGeometry(radius, 32, 32);

    // SIMPLE MATERIALS - NO TEXTURES TO AVOID WEBGL ERRORS
    let color;
    switch (planetName) {
        case 'mars':
            color = 0xff4444; // Red
            break;
        case 'earth':
            color = 0x4444ff; // Blue
            break;
        case 'aurelia':
            color = 0x44ff44; // Green
            break;
        default:
            color = 0xffff44; // Yellow
    }

    // Create simple material with NO TEXTURES
    const material = new THREE.MeshLambertMaterial({
        color: color,
        transparent: false,
        opacity: 1.0
    });

    const planet = new THREE.Mesh(geometry, material);

    // Position planets clearly visible
    const angle = (index / destinationsData.length) * Math.PI * 2;
    const distance = 8;

    planet.position.x = Math.cos(angle) * distance;
    planet.position.z = Math.sin(angle) * distance;
    planet.position.y = 0; // Keep at same level

    // Store database data
    planet.userData = destination;
    planet.name = destination.destinationName;

    // Make sure it's clickable
    planet.visible = true;

    console.log(`Created clickable planet: ${destination.destinationName} at position:`, planet.position);
    return planet;
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
        targetRotationX = Math.max(-Math.PI/3, Math.min(Math.PI/3, targetRotationX));

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

    // Recursive = true → checks children of groups
    const intersects = raycaster.intersectObjects(planets, true);

    console.log('Checking', planets.length, 'planets');
    console.log('Found', intersects.length, 'intersections', intersects);

    if (intersects.length > 0) {
        const clickedPlanet = intersects[0].object;
        console.log('PLANET CLICKED:', clickedPlanet.name || clickedPlanet.userData?.destinationName);

        selectPlanet(clickedPlanet);
    } else {
        console.log('No planet hit');
    }
}

function selectPlanet(planet) {
    selectedPlanet = planet;
    const destination = planet.userData;
    console.log('PLANET SELECTED:', destination.destinationName);

    updateInfoPanel(destination);
    updateStats();

    if (destination) {
        showPlanetPopup(destination);
    } else {
        console.warn("No destination data found for", planet.name);
    }
}

function updateInfoPanel(destination) {
    const infoPanel = document.getElementById('planet-info');
    if (infoPanel && destination) {
        infoPanel.innerHTML = `
            <h5 class="text-info mb-3">
                <i class="bi bi-globe me-2"></i>${destination.destinationName}
            </h5>
            <div class="mb-2">
                <strong>Database ID:</strong> ${destination.id}
            </div>
            <div class="mb-2">
                <strong>Type:</strong> ${destination.entityType || 'Unknown'}
            </div>
        `;
    }
}

function showPlanetPopup(destination) {
    document.getElementById("popup-title").textContent =
        `${destination.destinationName}`;
    document.getElementById("popup-type").textContent = destination.entityType;
    document.getElementById("popup-description").textContent = destination.description || "No description";
    document.getElementById("popup-gravity").textContent = destination.gravity || "No gravity";
    document.getElementById("popup-distance").textContent = destination.distanceFromEarth;

    const popup = document.getElementById("planet-popup");
    popup.style.display = "flex";

    document.getElementById("popup-close").onclick = closePopup;

    function closePopup() {
        popup.style.display = "none";
    }
}

function animate() {
    requestAnimationFrame(animate);

    // Update camera controls
    if (window.updateCameraControls) {
        window.updateCameraControls();
    }

    // Auto rotate planets
    if (autoRotate && planets.length > 0) {
        planets.forEach((planet, index) => {
            planet.rotation.y += 0.01;
            planet.rotation.x += 0.005;
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
    const modelsCount = document.getElementById('loaded-models');

    if (destCount) destCount.textContent = destinationsData.length;
    if (selectedName) selectedName.textContent = selectedPlanet ? selectedPlanet.name : 'None';
    if (modelsCount) modelsCount.textContent = planets.length;
}

function updateLoadingProgress(percentage, status) {
    const progressBar = document.getElementById('loading-progress');
    const statusText = document.getElementById('loading-status');

    if (progressBar) progressBar.style.width = percentage + '%';
    if (statusText) statusText.textContent = status;
}

function hideLoadingScreen() {
    const loadingScreen = document.getElementById('loading-screen');
    if (loadingScreen) {
        loadingScreen.style.opacity = '0';
        setTimeout(() => {
            loadingScreen.style.display = 'none';
        }, 300);
    }
}


// Initialize
document.addEventListener('DOMContentLoaded', () => {
    console.log('📄 DOM ready - Starting CLEAN space map...');
    setTimeout(() => {
        initSpaceMap();
    }, 200);
});

