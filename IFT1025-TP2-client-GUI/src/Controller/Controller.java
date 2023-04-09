package Controller;

/*
 * Cette classe lie le modele avec la vue. 
 * À noter qu'elle ne connaît ni de detailles d'implementation 
 * du comportement, ni de detailles de structuration du GUI.
 * 
 */
public class Controller {

	private Modele modele;
	private Vue vue;

	public Controller(Modele m, Vue v) {
		this.modele = m;
		this.vue = v;

		/* 
		 * La definition du comportement de chaque handler 
		 * est mise dans sa propre méthode auxiliaire. Il pourrait être même 
		 * dans sa propre classe entière: ne niveau de decouplage
		 * depend de la complexité de l'application
		 */
		
		this.vue.getIncButton().setOnAction((action) -> {
			this.inc();
		});

		this.vue.getDecButton().setOnAction((action) -> {
			this.dec();
		});

		this.vue.getDubButton().setOnAction((action) -> {
			this.dub();
		});

		this.vue.getDivButton().setOnAction((action) -> {
			this.div();
		});
	}

	private void inc() {
		this.modele.ajouter(1);
		this.vue.updateText(String.valueOf(this.modele.getValeur()));
	}

	private void dec() {
		this.modele.supprimer(1);
		this.vue.updateText(String.valueOf(this.modele.getValeur()));
	}

	private void dub() {
		this.modele.multiplier(2);
		this.vue.updateText(String.valueOf(this.modele.getValeur()));
	}

	private void div() {
		this.modele.diviser(2);
		this.vue.updateText(String.valueOf(this.modele.getValeur()));
	}

}