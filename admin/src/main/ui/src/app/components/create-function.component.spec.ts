import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { CreateFunctionComponent } from './create-function.component';

describe('CreateFunctionComponent', () => {
  let component: CreateFunctionComponent;
  let fixture: ComponentFixture<CreateFunctionComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ CreateFunctionComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(CreateFunctionComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
