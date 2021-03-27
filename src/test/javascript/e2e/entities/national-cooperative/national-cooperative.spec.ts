import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../page-objects/jhi-page-objects';

import {
  NationalCooperativeComponentsPage,
  NationalCooperativeDeleteDialog,
  NationalCooperativeUpdatePage,
} from './national-cooperative.page-object';

const expect = chai.expect;

describe('NationalCooperative e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let nationalCooperativeComponentsPage: NationalCooperativeComponentsPage;
  let nationalCooperativeUpdatePage: NationalCooperativeUpdatePage;
  let nationalCooperativeDeleteDialog: NationalCooperativeDeleteDialog;
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load NationalCooperatives', async () => {
    await navBarPage.goToEntity('national-cooperative');
    nationalCooperativeComponentsPage = new NationalCooperativeComponentsPage();
    await browser.wait(ec.visibilityOf(nationalCooperativeComponentsPage.title), 5000);
    expect(await nationalCooperativeComponentsPage.getTitle()).to.eq('coopcycleApp.nationalCooperative.home.title');
    await browser.wait(
      ec.or(ec.visibilityOf(nationalCooperativeComponentsPage.entities), ec.visibilityOf(nationalCooperativeComponentsPage.noResult)),
      1000
    );
  });

  it('should load create NationalCooperative page', async () => {
    await nationalCooperativeComponentsPage.clickOnCreateButton();
    nationalCooperativeUpdatePage = new NationalCooperativeUpdatePage();
    expect(await nationalCooperativeUpdatePage.getPageTitle()).to.eq('coopcycleApp.nationalCooperative.home.createOrEditLabel');
    await nationalCooperativeUpdatePage.cancel();
  });

  it('should create and save NationalCooperatives', async () => {
    const nbButtonsBeforeCreate = await nationalCooperativeComponentsPage.countDeleteButtons();

    await nationalCooperativeComponentsPage.clickOnCreateButton();

    await promise.all([nationalCooperativeUpdatePage.setNameInput('name')]);

    expect(await nationalCooperativeUpdatePage.getNameInput()).to.eq('name', 'Expected Name value to be equals to name');

    await nationalCooperativeUpdatePage.save();
    expect(await nationalCooperativeUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await nationalCooperativeComponentsPage.countDeleteButtons()).to.eq(
      nbButtonsBeforeCreate + 1,
      'Expected one more entry in the table'
    );
  });

  it('should delete last NationalCooperative', async () => {
    const nbButtonsBeforeDelete = await nationalCooperativeComponentsPage.countDeleteButtons();
    await nationalCooperativeComponentsPage.clickOnLastDeleteButton();

    nationalCooperativeDeleteDialog = new NationalCooperativeDeleteDialog();
    expect(await nationalCooperativeDeleteDialog.getDialogTitle()).to.eq('coopcycleApp.nationalCooperative.delete.question');
    await nationalCooperativeDeleteDialog.clickOnConfirmButton();
    await browser.wait(ec.visibilityOf(nationalCooperativeComponentsPage.title), 5000);

    expect(await nationalCooperativeComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
