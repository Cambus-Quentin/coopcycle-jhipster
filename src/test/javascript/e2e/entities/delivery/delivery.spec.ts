import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../page-objects/jhi-page-objects';

import { DeliveryComponentsPage, DeliveryDeleteDialog, DeliveryUpdatePage } from './delivery.page-object';

const expect = chai.expect;

describe('Delivery e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let deliveryComponentsPage: DeliveryComponentsPage;
  let deliveryUpdatePage: DeliveryUpdatePage;
  let deliveryDeleteDialog: DeliveryDeleteDialog;
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load Deliveries', async () => {
    await navBarPage.goToEntity('delivery');
    deliveryComponentsPage = new DeliveryComponentsPage();
    await browser.wait(ec.visibilityOf(deliveryComponentsPage.title), 5000);
    expect(await deliveryComponentsPage.getTitle()).to.eq('coopcycleApp.delivery.home.title');
    await browser.wait(ec.or(ec.visibilityOf(deliveryComponentsPage.entities), ec.visibilityOf(deliveryComponentsPage.noResult)), 1000);
  });

  it('should load create Delivery page', async () => {
    await deliveryComponentsPage.clickOnCreateButton();
    deliveryUpdatePage = new DeliveryUpdatePage();
    expect(await deliveryUpdatePage.getPageTitle()).to.eq('coopcycleApp.delivery.home.createOrEditLabel');
    await deliveryUpdatePage.cancel();
  });

  it('should create and save Deliveries', async () => {
    const nbButtonsBeforeCreate = await deliveryComponentsPage.countDeleteButtons();

    await deliveryComponentsPage.clickOnCreateButton();

    await promise.all([
      deliveryUpdatePage.setDeliveryAddrInput('deliveryAddr'),
      deliveryUpdatePage.setDistanceInput('5'),
      deliveryUpdatePage.setPriceInput('5'),
      deliveryUpdatePage.delivererSelectLastOption(),
    ]);

    expect(await deliveryUpdatePage.getDeliveryAddrInput()).to.eq(
      'deliveryAddr',
      'Expected DeliveryAddr value to be equals to deliveryAddr'
    );
    expect(await deliveryUpdatePage.getDistanceInput()).to.eq('5', 'Expected distance value to be equals to 5');
    expect(await deliveryUpdatePage.getPriceInput()).to.eq('5', 'Expected price value to be equals to 5');

    await deliveryUpdatePage.save();
    expect(await deliveryUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await deliveryComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
  });

  it('should delete last Delivery', async () => {
    const nbButtonsBeforeDelete = await deliveryComponentsPage.countDeleteButtons();
    await deliveryComponentsPage.clickOnLastDeleteButton();

    deliveryDeleteDialog = new DeliveryDeleteDialog();
    expect(await deliveryDeleteDialog.getDialogTitle()).to.eq('coopcycleApp.delivery.delete.question');
    await deliveryDeleteDialog.clickOnConfirmButton();
    await browser.wait(ec.visibilityOf(deliveryComponentsPage.title), 5000);

    expect(await deliveryComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
