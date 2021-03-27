import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../page-objects/jhi-page-objects';

import { LocalCooperativeComponentsPage, LocalCooperativeDeleteDialog, LocalCooperativeUpdatePage } from './local-cooperative.page-object';

const expect = chai.expect;

describe('LocalCooperative e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let localCooperativeComponentsPage: LocalCooperativeComponentsPage;
  let localCooperativeUpdatePage: LocalCooperativeUpdatePage;
  let localCooperativeDeleteDialog: LocalCooperativeDeleteDialog;
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load LocalCooperatives', async () => {
    await navBarPage.goToEntity('local-cooperative');
    localCooperativeComponentsPage = new LocalCooperativeComponentsPage();
    await browser.wait(ec.visibilityOf(localCooperativeComponentsPage.title), 5000);
    expect(await localCooperativeComponentsPage.getTitle()).to.eq('coopcycleApp.localCooperative.home.title');
    await browser.wait(
      ec.or(ec.visibilityOf(localCooperativeComponentsPage.entities), ec.visibilityOf(localCooperativeComponentsPage.noResult)),
      1000
    );
  });

  it('should load create LocalCooperative page', async () => {
    await localCooperativeComponentsPage.clickOnCreateButton();
    localCooperativeUpdatePage = new LocalCooperativeUpdatePage();
    expect(await localCooperativeUpdatePage.getPageTitle()).to.eq('coopcycleApp.localCooperative.home.createOrEditLabel');
    await localCooperativeUpdatePage.cancel();
  });

  it('should create and save LocalCooperatives', async () => {
    const nbButtonsBeforeCreate = await localCooperativeComponentsPage.countDeleteButtons();

    await localCooperativeComponentsPage.clickOnCreateButton();

    await promise.all([
      localCooperativeUpdatePage.setGeoZoneInput('geoZone'),
      localCooperativeUpdatePage.nationalCooperativeSelectLastOption(),
    ]);

    expect(await localCooperativeUpdatePage.getGeoZoneInput()).to.eq('geoZone', 'Expected GeoZone value to be equals to geoZone');

    await localCooperativeUpdatePage.save();
    expect(await localCooperativeUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await localCooperativeComponentsPage.countDeleteButtons()).to.eq(
      nbButtonsBeforeCreate + 1,
      'Expected one more entry in the table'
    );
  });

  it('should delete last LocalCooperative', async () => {
    const nbButtonsBeforeDelete = await localCooperativeComponentsPage.countDeleteButtons();
    await localCooperativeComponentsPage.clickOnLastDeleteButton();

    localCooperativeDeleteDialog = new LocalCooperativeDeleteDialog();
    expect(await localCooperativeDeleteDialog.getDialogTitle()).to.eq('coopcycleApp.localCooperative.delete.question');
    await localCooperativeDeleteDialog.clickOnConfirmButton();
    await browser.wait(ec.visibilityOf(localCooperativeComponentsPage.title), 5000);

    expect(await localCooperativeComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
