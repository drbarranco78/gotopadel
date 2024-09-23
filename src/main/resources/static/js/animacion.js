document.addEventListener("DOMContentLoaded", function() {
    const feat1 = document.getElementById('feat1');
    const feat2 = document.getElementById('feat2');
    const feat3 = document.getElementById('feat3');
    const feat4 = document.getElementById('feat4');

    const observer = new IntersectionObserver((entries, observer) => {
        entries.forEach(entry => {
            if (entry.isIntersecting) {
                // Aplicar la animación con un retraso escalonado
                if (entry.target === feat1) {
                    setTimeout(() => {
                        entry.target.classList.add("animacion-desplazar-derecha");
                    }, 0); // Sin retraso
                } else if (entry.target === feat2) {
                    setTimeout(() => {
                        entry.target.classList.add("animacion-desplazar-izquierda");
                    }, 1000); // Retraso de 1.5s
                } else if (entry.target === feat3) {
                    setTimeout(() => {
                        entry.target.classList.add("animacion-desplazar-derecha");
                    }, 2000); // Retraso de 3s
                } else if (entry.target === feat4) {
                    setTimeout(() => {
                        entry.target.classList.add("animacion-desplazar-izquierda");
                    }, 3000); // Retraso de 4.5s
                }
                observer.unobserve(entry.target); // Dejar de observar una vez que la animación haya empezado
            }
        });
    });

    // Observar todos los elementos
    observer.observe(feat1);
    observer.observe(feat2);
    observer.observe(feat3);
    observer.observe(feat4);

});
