import { TestBed, async } from '@angular/core/testing';
import { RouterTestingModule } from '@angular/router/testing';
import { SearchComponent } from './search.component';

describe('SearchComponent', () => {
  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [
        RouterTestingModule
      ],
      declarations: [
        SearchComponent
      ],
    }).compileComponents();
  }));

  it('should create the app', () => {
    const fixture = TestBed.createComponent(SearchComponent);
    const app = fixture.debugElement.componentInstance;
    expect(app).toBeTruthy();
  });

  it(`should have as title 'gui'`, () => {
    const fixture = TestBed.createComponent(SearchComponent);
    const app = fixture.debugElement.componentInstance;
    expect(app.title).toEqual('gui');
  });

  it('should render title in a h1 tag', () => {
    const fixture = TestBed.createComponent(SearchComponent);
    fixture.detectChanges();
    const compiled = fixture.debugElement.nativeElement;
    expect(compiled.querySelector('h1').textContent).toContain('Welcome to gui!');
  });
});