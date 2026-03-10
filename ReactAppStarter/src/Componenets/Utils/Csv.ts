
export interface CsvRow {
    [key: string] : string | number | boolean;
}

export const Csv = {
    convert: (rows: CsvRow[]): string => {
        if (!rows.length) return "";
        // create headers from keys:
        const headers = Object.keys(rows[0]);
        // convert array of string:
        const csvRows = [
            headers.join(","), // header row
            ...rows.map((row) =>
                headers.map((field) => JSON.stringify(row[field] ?? "")).join(",")
            )
        ];
        // generate csv string:
        return csvRows.join("\n");
    },
    writeToBinary: (csv: string): Blob => {
        // create blob
        const blob = new Blob([csv], { type: "text/csv;charset=utf-8;" });
        return blob;
    },
    download: (blob: Blob, filename: string) => {
        // create temporary download link:
        const url = URL.createObjectURL(blob);
        const link = document.createElement("a");
        link.href = url;
        link.setAttribute("download", filename);
        // augment the link-clicked & initiate browser download:
        document.body.appendChild(link);
        link.click();
        // cleanup:
        document.body.removeChild(link);
        URL.revokeObjectURL(url);
    },
}