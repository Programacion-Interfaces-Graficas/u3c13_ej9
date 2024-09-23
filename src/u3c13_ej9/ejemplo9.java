package u3c13_ej9;

class panaderia {
    int und; // Variable que almacena la cantidad de unidades de pan producidas
    boolean estado = false; // Indica si hay unidades disponibles para consumir

    // Método sincronizado para que el consumidor obtenga unidades
    public synchronized int get() {
        while (!estado) // Espera a que el productor haya producido algo
            try {
                wait(); // Suspende la ejecución hasta que se notifique que hay pan disponible
            } catch (InterruptedException e) {
                e.printStackTrace(); // Manejo de la excepción
            }
        System.out.println("Consumir: " + und); // Imprime la cantidad de pan consumido
        estado = false; // Cambia el estado a "sin unidades", indicando que ya se consumió el pan
        notify(); // Notifica al productor que puede producir más
        return und; // Retorna la unidad consumida
    }

    // Método sincronizado para que el productor coloque nuevas unidades
    public synchronized void put(int und) {
        while (estado) // Espera a que el consumidor haya consumido antes de producir más
            try {
                wait(); // Suspende la ejecución hasta que se notifique que el consumidor consumió
            } catch (InterruptedException e) {
                e.printStackTrace(); // Manejo de la excepción
            }
        this.und = und; // Asigna la nueva cantidad de pan producido
        estado = true; // Cambia el estado a "hay unidades", indicando que se ha producido pan
        System.out.println("Producir: " + this.und); // Imprime la cantidad de pan producido
        notify(); // Notifica al consumidor que puede consumir
    }
}

class productor implements Runnable {
    panaderia pan; // Referencia al objeto "panaderia"
    Thread hilo; // Hilo que ejecutará el productor

    public productor(panaderia pan_) {
        this.pan = pan_; // Asigna la panadería al productor
        hilo = new Thread(this); // Inicializa el hilo
    }

    public void run() {
        int i = 0;
        while (true) {
            pan.put(i++); // Produce una nueva unidad de pan
            try {
                hilo.sleep(1000); // Pausa la producción por 1 segundo para simular tiempo real
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

class consumidor implements Runnable {
    panaderia pan; // Referencia al objeto "panaderia"
    Thread hilo; // Hilo que ejecutará el consumidor

    public consumidor(panaderia pan_) {
        this.pan = pan_; // Asigna la panadería al consumidor
        hilo = new Thread(this); // Inicializa el hilo
    }

    public void run() {
        while (true) {
            pan.get(); // Consume una unidad de pan
            try {
                hilo.sleep(1000); // Pausa el consumo por 1 segundo para simular tiempo real
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

public class ejemplo9 {
    public static void main(String[] args) {
        panaderia ricoPan = new panaderia(); // Crea un objeto "panaderia"
        productor p = new productor(ricoPan); // Crea un productor asociado a la panadería
        consumidor c = new consumidor(ricoPan); // Crea un consumidor asociado a la panadería
        p.hilo.start(); // Inicia el hilo del productor
        c.hilo.start(); // Inicia el hilo del consumidor
    }
}
