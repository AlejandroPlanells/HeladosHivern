import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { HeladoComponent } from '../list/helado.component';
import { HeladoDetailComponent } from '../detail/helado-detail.component';
import { HeladoUpdateComponent } from '../update/helado-update.component';
import { HeladoRoutingResolveService } from './helado-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const heladoRoute: Routes = [
  {
    path: '',
    component: HeladoComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: HeladoDetailComponent,
    resolve: {
      helado: HeladoRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: HeladoUpdateComponent,
    resolve: {
      helado: HeladoRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: HeladoUpdateComponent,
    resolve: {
      helado: HeladoRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(heladoRoute)],
  exports: [RouterModule],
})
export class HeladoRoutingModule {}
