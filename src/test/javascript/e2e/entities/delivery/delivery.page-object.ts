import { element, by, ElementFinder } from 'protractor';

export class DeliveryComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('jhi-delivery div table .btn-danger'));
  title = element.all(by.css('jhi-delivery div h2#page-heading span')).first();
  noResult = element(by.id('no-result'));
  entities = element(by.id('entities'));

  async clickOnCreateButton(): Promise<void> {
    await this.createButton.click();
  }

  async clickOnLastDeleteButton(): Promise<void> {
    await this.deleteButtons.last().click();
  }

  async countDeleteButtons(): Promise<number> {
    return this.deleteButtons.count();
  }

  async getTitle(): Promise<string> {
    return this.title.getAttribute('jhiTranslate');
  }
}

export class DeliveryUpdatePage {
  pageTitle = element(by.id('jhi-delivery-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));

  idInput = element(by.id('field_id'));
  deliveryAddrInput = element(by.id('field_deliveryAddr'));
  distanceInput = element(by.id('field_distance'));
  priceInput = element(by.id('field_price'));

  delivererSelect = element(by.id('field_deliverer'));

  async getPageTitle(): Promise<string> {
    return this.pageTitle.getAttribute('jhiTranslate');
  }

  async setIdInput(id: string): Promise<void> {
    await this.idInput.sendKeys(id);
  }

  async getIdInput(): Promise<string> {
    return await this.idInput.getAttribute('value');
  }

  async setDeliveryAddrInput(deliveryAddr: string): Promise<void> {
    await this.deliveryAddrInput.sendKeys(deliveryAddr);
  }

  async getDeliveryAddrInput(): Promise<string> {
    return await this.deliveryAddrInput.getAttribute('value');
  }

  async setDistanceInput(distance: string): Promise<void> {
    await this.distanceInput.sendKeys(distance);
  }

  async getDistanceInput(): Promise<string> {
    return await this.distanceInput.getAttribute('value');
  }

  async setPriceInput(price: string): Promise<void> {
    await this.priceInput.sendKeys(price);
  }

  async getPriceInput(): Promise<string> {
    return await this.priceInput.getAttribute('value');
  }

  async delivererSelectLastOption(): Promise<void> {
    await this.delivererSelect.all(by.tagName('option')).last().click();
  }

  async delivererSelectOption(option: string): Promise<void> {
    await this.delivererSelect.sendKeys(option);
  }

  getDelivererSelect(): ElementFinder {
    return this.delivererSelect;
  }

  async getDelivererSelectedOption(): Promise<string> {
    return await this.delivererSelect.element(by.css('option:checked')).getText();
  }

  async save(): Promise<void> {
    await this.saveButton.click();
  }

  async cancel(): Promise<void> {
    await this.cancelButton.click();
  }

  getSaveButton(): ElementFinder {
    return this.saveButton;
  }
}

export class DeliveryDeleteDialog {
  private dialogTitle = element(by.id('jhi-delete-delivery-heading'));
  private confirmButton = element(by.id('jhi-confirm-delete-delivery'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getAttribute('jhiTranslate');
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}
