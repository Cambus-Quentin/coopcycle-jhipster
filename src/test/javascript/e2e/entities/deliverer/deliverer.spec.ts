import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../page-objects/jhi-page-objects';

import { DelivererComponentsPage, DelivererDeleteDialog, DelivererUpdatePage } from './deliverer.page-object';

const expect = chai.expect;

describe('Deliverer e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let delivererComponentsPage: DelivererComponentsPage;
  let delivererUpdatePage: DelivererUpdatePage;
  let delivererDeleteDialog: DelivererDeleteDialog;
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load Deliverers', async () => {
    await navBarPage.goToEntity('deliverer');
    delivererComponentsPage = new DelivererComponentsPage();
    await browser.wait(ec.visibilityOf(delivererComponentsPage.title), 5000);
    expect(await delivererComponentsPage.getTitle()).to.eq('coopcycleApp.deliverer.home.title');
    await browser.wait(ec.or(ec.visibilityOf(delivererComponentsPage.entities), ec.visibilityOf(delivererComponentsPage.noResult)), 1000);
  });

  it('should load create Deliverer page', async () => {
    await delivererComponentsPage.clickOnCreateButton();
    delivererUpdatePage = new DelivererUpdatePage();
    expect(await delivererUpdatePage.getPageTitle()).to.eq('coopcycleApp.deliverer.home.createOrEditLabel');
    await delivererUpdatePage.cancel();
  });

  it('should create and save Deliverers', async () => {
    const nbButtonsBeforeCreate = await delivererComponentsPage.countDeleteButtons();

    await delivererComponentsPage.clickOnCreateButton();

    await promise.all([
      delivererUpdatePage.setNameInput('name'),
      delivererUpdatePage.setFirstnameInput('firstname'),
      delivererUpdatePage.userSelectLastOption(),
    ]);

    expect(await delivererUpdatePage.getNameInput()).to.eq('name', 'Expected Name value to be equals to name');
    expect(await delivererUpdatePage.getFirstnameInput()).to.eq('firstname', 'Expected Firstname value to be equals to firstname');

    await delivererUpdatePage.save();
    expect(await delivererUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await delivererComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
  });

  it('should delete last Deliverer', async () => {
    const nbButtonsBeforeDelete = await delivererComponentsPage.countDeleteButtons();
    await delivererComponentsPage.clickOnLastDeleteButton();

    delivererDeleteDialog = new DelivererDeleteDialog();
    expect(await delivererDeleteDialog.getDialogTitle()).to.eq('coopcycleApp.deliverer.delete.question');
    await delivererDeleteDialog.clickOnConfirmButton();
    await browser.wait(ec.visibilityOf(delivererComponentsPage.title), 5000);

    expect(await delivererComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
