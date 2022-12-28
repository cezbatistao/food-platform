package json

type ListResponse struct {
    Data       []interface{} `json:"data"`
    Page       int           `json:"page"`
    TotalPages int           `json:"total_pages"`
    Size       int           `json:"size"`
    Total      int           `json:"total"`
}
