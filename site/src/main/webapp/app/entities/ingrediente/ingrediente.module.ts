import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { IngredienteComponent } from './list/ingrediente.component';
import { IngredienteDetailComponent } from './detail/ingrediente-detail.component';
import { IngredienteUpdateComponent } from './update/ingrediente-update.component';
import { IngredienteDeleteDialogComponent } from './delete/ingrediente-delete-dialog.component';
import { IngredienteRoutingModule } from './route/ingrediente-routing.module';

@NgModule({
  imports: [SharedModule, IngredienteRoutingModule],
  declarations: [IngredienteComponent, IngredienteDetailComponent, IngredienteUpdateComponent, IngredienteDeleteDialogComponent],
})
export class IngredienteModule {}
