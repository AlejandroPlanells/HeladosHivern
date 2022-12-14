import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IFabricante } from '../fabricante.model';
import { FabricanteService } from '../service/fabricante.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './fabricante-delete-dialog.component.html',
})
export class FabricanteDeleteDialogComponent {
  fabricante?: IFabricante;

  constructor(protected fabricanteService: FabricanteService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.fabricanteService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
