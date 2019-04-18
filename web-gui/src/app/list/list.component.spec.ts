/**
 * THIS PROJECT IS A UNIVERSITY PROJECT AND MADE BY SCHOOL FOR HIGHER AND PROFESSIONAL EDUCATION WITH COVENTRY UNIVERSITY (UK).
 * PLEASE BE MIND THAT YOU COULD NOT COPY, MODIFY OR SHARE THIS PROJECT IF YOU ARE NOT GET PERMISSION.
 * AUTHOR INFORMATION:
 * NAME: TSANG Long Fung (187107130)
 * CONTACT NUMBER: (+852) 6679 2339
 * CONTACT EMAIL: 187107130@stu.vtc.edu.hk
 */

import { TestBed, async } from '@angular/core/testing';
import { RouterTestingModule } from '@angular/router/testing';
import { ListComponent } from './list.component';

describe('ListComponent', () => {
  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [
        RouterTestingModule
      ],
      declarations: [
        ListComponent
      ],
    }).compileComponents();
  }));

  it('should create the app', () => {
    const fixture = TestBed.createComponent(ListComponent);
    const app = fixture.debugElement.componentInstance;
    expect(app).toBeTruthy();
  });

  it(`should have as title 'gui'`, () => {
    const fixture = TestBed.createComponent(ListComponent);
    const app = fixture.debugElement.componentInstance;
    expect(app.title).toEqual('gui');
  });

  it('should render title in a h1 tag', () => {
    const fixture = TestBed.createComponent(ListComponent);
    fixture.detectChanges();
    const compiled = fixture.debugElement.nativeElement;
    expect(compiled.querySelector('h1').textContent).toContain('Welcome to gui!');
  });
});
