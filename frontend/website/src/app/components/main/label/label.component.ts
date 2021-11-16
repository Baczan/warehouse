import {ChangeDetectorRef, Component, ElementRef, Input, OnInit, ViewChild, ViewEncapsulation} from '@angular/core';
import {environment} from "../../../../environments/environment";

@Component({
  selector: 'app-label',
  templateUrl: './label.component.html',
  styleUrls: ['./label.component.css'],
  encapsulation:ViewEncapsulation.None
})
export class LabelComponent implements OnInit {

  @Input() partName:string;
  @Input() warehouseNumber:string;
  @Input() car:string;
  @Input() id:number;
  @Input() size:string = "500";
  @ViewChild("main") main:ElementRef;

  elementReference:ElementRef;

  constructor(private elReference:ElementRef,private cdr:ChangeDetectorRef) {
    this.elementReference = elReference;
  }


  ngOnInit(): void {
  }

  print(){

    this.cdr.detectChanges();

    let okno = window.open("", "Drukuj", "height=1000,width=1000");
    okno.document.write("<html lang=\"en\">\n" +
      "<head>\n" +
      "  <meta charset=\"utf-8\">\n" +
      "  <title>Untitled</title>\n" +
      "  <meta name=\"viewport\" content=\"width=device-width, initial-scale=1\">\n" +
      "  <link rel=\"icon\" type=\"image/x-icon\" href=\"favicon.ico\">\n" +
      "  <link href=\"https://fonts.googleapis.com/css?family=Roboto:300,400,500&display=swap\" rel=\"stylesheet\">\n" +
      "  <link href=\"https://fonts.googleapis.com/icon?family=Material+Icons\" rel=\"stylesheet\">\n" +
      "  <link href=\""+environment.PRINT_STYLE_URL+"\" rel=\"stylesheet\">\n" +
      "</head>\n");
    okno.document.write(this.main.nativeElement.innerHTML);
    okno.document.close();
    okno.onload = (e)=>{
      okno.print();
    };

  }

  getSize(){
    return `width: ${this.size}px`
  }

  getId(){
    return `#`+this.id;
  }



}
