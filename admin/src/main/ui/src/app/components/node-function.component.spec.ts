import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { NodeFunctionComponent } from './node-function.component';

describe('NodeFunctionComponent', () => {
  let component: NodeFunctionComponent;
  let fixture: ComponentFixture<NodeFunctionComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ NodeFunctionComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(NodeFunctionComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
