import { utils, WorkBook, WorkSheet, write, writeFile } from "xlsx"

export interface ExcelRow {
    [key: string] : string | number | boolean;
}

export const Excel = {
    worksheet: (rows: ExcelRow[]): WorkSheet | null => {
        if (!rows.length) return null;
        // headers from keys:
        const headers = Object.keys(rows[0]);
        // convert JSON to worksheet
        const worksheet = utils.json_to_sheet(rows, {
            header: headers
        });
        return worksheet;
    },
    workbook: (worksheet: WorkSheet, sheetName: string): WorkBook => {
        // create workbook
        const workbook = utils.book_new();
        utils.book_append_sheet(workbook, worksheet, sheetName);
        return workbook;
    },
    append: (workbook: WorkBook, worksheet: WorkSheet, sheetName: string): void => {
        utils.book_append_sheet(workbook, worksheet, sheetName);
    },
    writeToBinary: (workbook: WorkBook): Blob => {
        // create blob:
        const excelBinary = write(workbook, { bookType: "xlsx", type: "array" });
        const blob = new Blob([excelBinary], {
            type: "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
        });
        return blob;
    },
    download: (workbook: WorkBook, filename: string): void => {
        // start download immediately:
        writeFile(workbook, filename);
    },
}