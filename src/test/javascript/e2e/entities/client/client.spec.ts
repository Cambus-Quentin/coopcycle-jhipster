import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../page-objects/jhi-page-objects';

import { ClientComponentsPage, ClientDeleteDialog, ClientUpdatePage } from './client.page-object';
import * as path from 'path';

const expect = chai.expect;

describe('Client e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let clientComponentsPage: ClientComponentsPage;
  let clientUpdatePage: ClientUpdatePage;
  let clientDeleteDialog: ClientDeleteDialog;
  const fileNameToUpload = 'logo-jhipster.png';
  const fileToUpload = '../../../../../../src/main/webapp/content/images/' + fileNameToUpload;
  const absolutePath = path.resolve(__dirname, fileToUpload);
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load Clients', async () => {
    await navBarPage.goToEntity('client');
    clientComponentsPage = new ClientComponentsPage();
    await browser.wait(ec.visibilityOf(clientComponentsPage.title), 5000);
    expect(await clientComponentsPage.getTitle()).to.eq('coopcycleApp.client.home.title');
    await browser.wait(ec.or(ec.visibilityOf(clientComponentsPage.entities), ec.visibilityOf(clientComponentsPage.noResult)), 1000);
  });

  it('should load create Client page', async () => {
    await clientComponentsPage.clickOnCreateButton();
    clientUpdatePage = new ClientUpdatePage();
    expect(await clientUpdatePage.getPageTitle()).to.eq('coopcycleApp.client.home.createOrEditLabel');
    await clientUpdatePage.cancel();
  });

  it('should create and save Clients', async () => {
    const nbButtonsBeforeCreate = await clientComponentsPage.countDeleteButtons();

    await clientComponentsPage.clickOnCreateButton();

    await promise.all([
      clientUpdatePage.setNameInput('name'),
      clientUpdatePage.setSurnameInput('surname'),
      clientUpdatePage.setAddressInput('address'),
      clientUpdatePage.setPhoneNumberInput('phoneNumber'),
      clientUpdatePage.setEmailInput('email'),
      clientUpdatePage.setProfilInput(absolutePath),
      clientUpdatePage.userSelectLastOption(),
    ]);

    expect(await clientUpdatePage.getNameInput()).to.eq('name', 'Expected Name value to be equals to name');
    expect(await clientUpdatePage.getSurnameInput()).to.eq('surname', 'Expected Surname value to be equals to surname');
    expect(await clientUpdatePage.getAddressInput()).to.eq('address', 'Expected Address value to be equals to address');
    expect(await clientUpdatePage.getPhoneNumberInput()).to.eq('phoneNumber', 'Expected PhoneNumber value to be equals to phoneNumber');
    expect(await clientUpdatePage.getEmailInput()).to.eq('email', 'Expected Email value to be equals to email');
    expect(await clientUpdatePage.getProfilInput()).to.endsWith(
      fileNameToUpload,
      'Expected Profil value to be end with ' + fileNameToUpload
    );

    await clientUpdatePage.save();
    expect(await clientUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await clientComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
  });

  it('should delete last Client', async () => {
    const nbButtonsBeforeDelete = await clientComponentsPage.countDeleteButtons();
    await clientComponentsPage.clickOnLastDeleteButton();

    clientDeleteDialog = new ClientDeleteDialog();
    expect(await clientDeleteDialog.getDialogTitle()).to.eq('coopcycleApp.client.delete.question');
    await clientDeleteDialog.clickOnConfirmButton();
    await browser.wait(ec.visibilityOf(clientComponentsPage.title), 5000);

    expect(await clientComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
