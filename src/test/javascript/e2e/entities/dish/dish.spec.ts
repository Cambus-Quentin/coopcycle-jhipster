import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../page-objects/jhi-page-objects';

import { DishComponentsPage, DishDeleteDialog, DishUpdatePage } from './dish.page-object';

const expect = chai.expect;

describe('Dish e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let dishComponentsPage: DishComponentsPage;
  let dishUpdatePage: DishUpdatePage;
  let dishDeleteDialog: DishDeleteDialog;
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load Dishes', async () => {
    await navBarPage.goToEntity('dish');
    dishComponentsPage = new DishComponentsPage();
    await browser.wait(ec.visibilityOf(dishComponentsPage.title), 5000);
    expect(await dishComponentsPage.getTitle()).to.eq('coopcycleApp.dish.home.title');
    await browser.wait(ec.or(ec.visibilityOf(dishComponentsPage.entities), ec.visibilityOf(dishComponentsPage.noResult)), 1000);
  });

  it('should load create Dish page', async () => {
    await dishComponentsPage.clickOnCreateButton();
    dishUpdatePage = new DishUpdatePage();
    expect(await dishUpdatePage.getPageTitle()).to.eq('coopcycleApp.dish.home.createOrEditLabel');
    await dishUpdatePage.cancel();
  });

  it('should create and save Dishes', async () => {
    const nbButtonsBeforeCreate = await dishComponentsPage.countDeleteButtons();

    await dishComponentsPage.clickOnCreateButton();

    await promise.all([
      dishUpdatePage.setNameInput('name'),
      dishUpdatePage.setPriceInput('5'),
      dishUpdatePage.restaurantSelectLastOption(),
    ]);

    expect(await dishUpdatePage.getNameInput()).to.eq('name', 'Expected Name value to be equals to name');
    expect(await dishUpdatePage.getPriceInput()).to.eq('5', 'Expected price value to be equals to 5');

    await dishUpdatePage.save();
    expect(await dishUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await dishComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
  });

  it('should delete last Dish', async () => {
    const nbButtonsBeforeDelete = await dishComponentsPage.countDeleteButtons();
    await dishComponentsPage.clickOnLastDeleteButton();

    dishDeleteDialog = new DishDeleteDialog();
    expect(await dishDeleteDialog.getDialogTitle()).to.eq('coopcycleApp.dish.delete.question');
    await dishDeleteDialog.clickOnConfirmButton();
    await browser.wait(ec.visibilityOf(dishComponentsPage.title), 5000);

    expect(await dishComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
