import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'fabricante',
        data: { pageTitle: 'jHipsterGApp.fabricante.home.title' },
        loadChildren: () => import('./fabricante/fabricante.module').then(m => m.FabricanteModule),
      },
      {
        path: 'helado',
        data: { pageTitle: 'jHipsterGApp.helado.home.title' },
        loadChildren: () => import('./helado/helado.module').then(m => m.HeladoModule),
      },
      {
        path: 'ingrediente',
        data: { pageTitle: 'jHipsterGApp.ingrediente.home.title' },
        loadChildren: () => import('./ingrediente/ingrediente.module').then(m => m.IngredienteModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
