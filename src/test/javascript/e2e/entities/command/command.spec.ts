import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../page-objects/jhi-page-objects';

import { CommandComponentsPage, CommandDeleteDialog, CommandUpdatePage } from './command.page-object';

const expect = chai.expect;

describe('Command e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let commandComponentsPage: CommandComponentsPage;
  let commandUpdatePage: CommandUpdatePage;
  let commandDeleteDialog: CommandDeleteDialog;
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load Commands', async () => {
    await navBarPage.goToEntity('command');
    commandComponentsPage = new CommandComponentsPage();
    await browser.wait(ec.visibilityOf(commandComponentsPage.title), 5000);
    expect(await commandComponentsPage.getTitle()).to.eq('coopcycleApp.command.home.title');
    await browser.wait(ec.or(ec.visibilityOf(commandComponentsPage.entities), ec.visibilityOf(commandComponentsPage.noResult)), 1000);
  });

  it('should load create Command page', async () => {
    await commandComponentsPage.clickOnCreateButton();
    commandUpdatePage = new CommandUpdatePage();
    expect(await commandUpdatePage.getPageTitle()).to.eq('coopcycleApp.command.home.createOrEditLabel');
    await commandUpdatePage.cancel();
  });

  it('should create and save Commands', async () => {
    const nbButtonsBeforeCreate = await commandComponentsPage.countDeleteButtons();

    await commandComponentsPage.clickOnCreateButton();

    await promise.all([
      commandUpdatePage.setDateInput('2000-12-31'),
      commandUpdatePage.setPriceInput('5'),
      commandUpdatePage.stateSelectLastOption(),
      commandUpdatePage.clientSelectLastOption(),
      commandUpdatePage.deliverySelectLastOption(),
      commandUpdatePage.restaurantSelectLastOption(),
      // commandUpdatePage.dishSelectLastOption(),
    ]);

    expect(await commandUpdatePage.getDateInput()).to.eq('2000-12-31', 'Expected date value to be equals to 2000-12-31');
    expect(await commandUpdatePage.getPriceInput()).to.eq('5', 'Expected price value to be equals to 5');

    await commandUpdatePage.save();
    expect(await commandUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await commandComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
  });

  it('should delete last Command', async () => {
    const nbButtonsBeforeDelete = await commandComponentsPage.countDeleteButtons();
    await commandComponentsPage.clickOnLastDeleteButton();

    commandDeleteDialog = new CommandDeleteDialog();
    expect(await commandDeleteDialog.getDialogTitle()).to.eq('coopcycleApp.command.delete.question');
    await commandDeleteDialog.clickOnConfirmButton();
    await browser.wait(ec.visibilityOf(commandComponentsPage.title), 5000);

    expect(await commandComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
