import { ComponentFixture, TestBed } from '@angular/core/testing';

import { VendaFormComponent } from './venda-form.component';

describe('VendaFormComponent', () => {
  let component: VendaFormComponent;
  let fixture: ComponentFixture<VendaFormComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [VendaFormComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(VendaFormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
