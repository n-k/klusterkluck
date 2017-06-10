import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { NodeConnectorComponent } from './node-connector.component';

describe('NodeConnectorComponent', () => {
  let component: NodeConnectorComponent;
  let fixture: ComponentFixture<NodeConnectorComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ NodeConnectorComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(NodeConnectorComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
