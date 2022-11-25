import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IIngrediente } from '../ingrediente.model';
import { IngredienteService } from '../service/ingrediente.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './ingrediente-delete-dialog.component.html',
})
export class IngredienteDeleteDialogComponent {
  ingrediente?: IIngrediente;

  constructor(protected ingredienteService: IngredienteService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.ingredienteService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
