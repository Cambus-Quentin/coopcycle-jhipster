import { element, by, ElementFinder } from 'protractor';

export class CommandComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('jhi-command div table .btn-danger'));
  title = element.all(by.css('jhi-command div h2#page-heading span')).first();
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

export class CommandUpdatePage {
  pageTitle = element(by.id('jhi-command-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));

  idInput = element(by.id('field_id'));
  dateInput = element(by.id('field_date'));
  priceInput = element(by.id('field_price'));
  stateSelect = element(by.id('field_state'));

  clientSelect = element(by.id('field_client'));
  deliverySelect = element(by.id('field_delivery'));
  restaurantSelect = element(by.id('field_restaurant'));
  dishSelect = element(by.id('field_dish'));

  async getPageTitle(): Promise<string> {
    return this.pageTitle.getAttribute('jhiTranslate');
  }

  async setIdInput(id: string): Promise<void> {
    await this.idInput.sendKeys(id);
  }

  async getIdInput(): Promise<string> {
    return await this.idInput.getAttribute('value');
  }

  async setDateInput(date: string): Promise<void> {
    await this.dateInput.sendKeys(date);
  }

  async getDateInput(): Promise<string> {
    return await this.dateInput.getAttribute('value');
  }

  async setPriceInput(price: string): Promise<void> {
    await this.priceInput.sendKeys(price);
  }

  async getPriceInput(): Promise<string> {
    return await this.priceInput.getAttribute('value');
  }

  async setStateSelect(state: string): Promise<void> {
    await this.stateSelect.sendKeys(state);
  }

  async getStateSelect(): Promise<string> {
    return await this.stateSelect.element(by.css('option:checked')).getText();
  }

  async stateSelectLastOption(): Promise<void> {
    await this.stateSelect.all(by.tagName('option')).last().click();
  }

  async clientSelectLastOption(): Promise<void> {
    await this.clientSelect.all(by.tagName('option')).last().click();
  }

  async clientSelectOption(option: string): Promise<void> {
    await this.clientSelect.sendKeys(option);
  }

  getClientSelect(): ElementFinder {
    return this.clientSelect;
  }

  async getClientSelectedOption(): Promise<string> {
    return await this.clientSelect.element(by.css('option:checked')).getText();
  }

  async deliverySelectLastOption(): Promise<void> {
    await this.deliverySelect.all(by.tagName('option')).last().click();
  }

  async deliverySelectOption(option: string): Promise<void> {
    await this.deliverySelect.sendKeys(option);
  }

  getDeliverySelect(): ElementFinder {
    return this.deliverySelect;
  }

  async getDeliverySelectedOption(): Promise<string> {
    return await this.deliverySelect.element(by.css('option:checked')).getText();
  }

  async restaurantSelectLastOption(): Promise<void> {
    await this.restaurantSelect.all(by.tagName('option')).last().click();
  }

  async restaurantSelectOption(option: string): Promise<void> {
    await this.restaurantSelect.sendKeys(option);
  }

  getRestaurantSelect(): ElementFinder {
    return this.restaurantSelect;
  }

  async getRestaurantSelectedOption(): Promise<string> {
    return await this.restaurantSelect.element(by.css('option:checked')).getText();
  }

  async dishSelectLastOption(): Promise<void> {
    await this.dishSelect.all(by.tagName('option')).last().click();
  }

  async dishSelectOption(option: string): Promise<void> {
    await this.dishSelect.sendKeys(option);
  }

  getDishSelect(): ElementFinder {
    return this.dishSelect;
  }

  async getDishSelectedOption(): Promise<string> {
    return await this.dishSelect.element(by.css('option:checked')).getText();
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

export class CommandDeleteDialog {
  private dialogTitle = element(by.id('jhi-delete-command-heading'));
  private confirmButton = element(by.id('jhi-confirm-delete-command'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getAttribute('jhiTranslate');
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}
