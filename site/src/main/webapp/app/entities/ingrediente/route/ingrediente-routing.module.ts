import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { IngredienteComponent } from '../list/ingrediente.component';
import { IngredienteDetailComponent } from '../detail/ingrediente-detail.component';
import { IngredienteUpdateComponent } from '../update/ingrediente-update.component';
import { IngredienteRoutingResolveService } from './ingrediente-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const ingredienteRoute: Routes = [
  {
    path: '',
    component: IngredienteComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: IngredienteDetailComponent,
    resolve: {
      ingrediente: IngredienteRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: IngredienteUpdateComponent,
    resolve: {
      ingrediente: IngredienteRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: IngredienteUpdateComponent,
    resolve: {
      ingrediente: IngredienteRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(ingredienteRoute)],
  exports: [RouterModule],
})
export class IngredienteRoutingModule {}
