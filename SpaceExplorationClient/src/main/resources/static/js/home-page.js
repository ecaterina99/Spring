function scrollToTop() {
    window.scrollTo({
        top: 0,
        behavior: 'smooth'
    });
}

function createParticles() {
    const particleCount = 20;
    const background = document.querySelector('.space-background-result');
    for (let i = 0; i < particleCount; i++) {
        const particle = document.createElement('div');
        particle.className = 'particle';
        particle.style.left = Math.random() * 100 + '%';
        particle.style.width = (Math.random() * 4 + 2) + 'px';
        particle.style.height = particle.style.width;
        particle.style.animationDelay = Math.random() * 8 + 's';
        particle.style.animationDuration = (Math.random() * 6 + 6) + 's';
        background.appendChild(particle);
    }
}

window.addEventListener('scroll', () => {
    const scrolled = window.pageYOffset;
    const stars1 = document.querySelector('.stars');
    const stars2 = document.querySelector('.stars2');
    const stars3 = document.querySelector('.stars3');

    if (stars1) stars1.style.transform = `translateY(${scrolled * 0.5}px)`;
    if (stars2) stars2.style.transform = `translateY(${scrolled * 0.8}px)`;
    if (stars3) stars3.style.transform = `translateY(${scrolled * 0.9}px)`;
});

const observerOptions = {
    threshold: 0.1,
    rootMargin: '0px 0px -100px 0px'
};

const observer = new IntersectionObserver((entries) => {
    entries.forEach(entry => {
        if (entry.isIntersecting) {
            entry.target.style.opacity = '1';
            entry.target.style.transform = 'translateY(0)';
        }
    });
}, observerOptions);

document.addEventListener('DOMContentLoaded', () => {
    createParticles();

    document.querySelectorAll('.neon-section, .nav-card, .rule-item, .advice-card').forEach(el => {
        el.style.opacity = '0';
        el.style.transform = 'translateY(50px)';
        el.style.transition = 'all 0.8s ease';
        observer.observe(el);
    });

    setInterval(() => {
        const shootingStar = document.createElement('div');
        shootingStar.className = 'shooting-star';
        shootingStar.style.top = Math.random() * 50 + '%';
        shootingStar.style.left = Math.random() * 50 + '%';
        shootingStar.style.animationDelay = '0s';
        document.querySelector('.space-background-result').appendChild(shootingStar);

        setTimeout(() => shootingStar.remove(), 3000);
    }, 5000);
});

document.querySelectorAll('.nav-card, .cta-button').forEach(el => {
    el.addEventListener('mouseenter', () => {
        el.style.transition = 'all 0.3s ease';
    });
});

const cards = document.querySelectorAll('.neon-section, .nav-card');
cards.forEach((card, index) => {
    card.style.animationDelay = `${index * 0.1}s`;
});