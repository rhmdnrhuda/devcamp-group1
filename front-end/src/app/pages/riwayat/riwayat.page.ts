import { Component, OnInit } from '@angular/core';


@Component({
  selector: 'app-riwayat',
  templateUrl: './riwayat.page.html',
  styleUrls: ['./riwayat.page.scss'],
})
export class RiwayatPage implements OnInit {
  balance: Number = 90000000;
  dateNow;
  dateFirstThisMonth;

  data = [
    {
        "data": [
            {
                "Category": "Food",
                "Amount": 20000000,
                "Type": "Expense",
                "Timestamp": "2019-08-30 13:52:50.0",
                "Note": " "
            },
            {
                "Category": "Food",
                "Amount": 200000,
                "Type": "Income",
                "Timestamp": "2019-08-30 12:03:59.0",
                "Note": " "
            }
        ],
        "tanggal": "2019-08-30",
        "total": 2000900,
    },
    {
        "data": [
            {
                "Category": "Food",
                "Amount": 200000,
                "Type": "Expense",
                "Timestamp": "2019-08-29 21:49:39.0",
                "Note": " "
            },
            {
                "Category": "Food",
                "Amount": 200000,
                "Type": "Expense",
                "Timestamp": "2019-08-29 21:12:17.0",
                "Note": " "
            }
        ],
        "tanggal": "2019-08-29",
        "total": -2000900,
    }
];

  constructor() { }

  ngOnInit() {
    this.setDate();
  }

  setDate(){
    this.dateNow = Date.now();

    console.log(this.dateNow)
  }

}
