import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { HeladoComponent } from './list/helado.component';
import { HeladoDetailComponent } from './detail/helado-detail.component';
import { HeladoUpdateComponent } from './update/helado-update.component';
import { HeladoDeleteDialogComponent } from './delete/helado-delete-dialog.component';
import { HeladoRoutingModule } from './route/helado-routing.module';

@NgModule({
  imports: [SharedModule, HeladoRoutingModule],
  declarations: [HeladoComponent, HeladoDetailComponent, HeladoUpdateComponent, HeladoDeleteDialogComponent],
})
export class HeladoModule {}
